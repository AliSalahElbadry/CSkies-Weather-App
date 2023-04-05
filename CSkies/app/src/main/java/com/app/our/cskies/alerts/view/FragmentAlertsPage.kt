package com.app.our.cskies.alerts.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.our.cskies.locationGetter.FragmentLocationDetector
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.alerts.utils.AlarmUtils
import com.app.our.cskies.alerts.viewmodel.AlertViewModelFactory
import com.app.our.cskies.alerts.viewmodel.AlertsViewModel
import com.app.our.cskies.databinding.FragmentAlertsPageBinding
import com.app.our.cskies.dp.AppDataBase
import com.app.our.cskies.dp.LocalSourceImpl
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.network.RemoteSourceImpl
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserStates
import kotlinx.coroutines.launch


class FragmentAlertsPage : Fragment(),OnClickDeleteAlert{
    var isLocationMapSet: Boolean=false
    lateinit var  binding:FragmentAlertsPageBinding
    private lateinit var alertsViewModel: AlertsViewModel
    lateinit var adapter: AlertsAdapter
    lateinit var  factory:AlertViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding=FragmentAlertsPageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= AlertsAdapter(listOf(),this)
        binding.recyclerView.layoutManager=LinearLayoutManager(this.context)
        binding.recyclerView.adapter=adapter
        factory= AlertViewModelFactory(Repository.getInstance(
            LocalSourceImpl.getInstance(AppDataBase.getInstance(requireActivity().applicationContext).getLocationDao()),
            RemoteSourceImpl.getInstance()!!))
        alertsViewModel=ViewModelProvider(this,factory)[AlertsViewModel::class.java]
        alertsViewModel.getAllAlerts()
        lifecycleScope.launch {
            alertsViewModel.alertSetter.observe(viewLifecycleOwner, Observer {
                Log.e("","Added............page")
                if(it.getId()!=null) {
                    AlarmUtils.setAlarm(requireContext(), it)
                    Dialogs.SnakeToast(
                        binding.recyclerView,
                        if (Setting.getLang() == "en") "Done Adding New Alert" else "تم اضافة تنبيه جديد"
                    )
                }
            })
        }
        alertsViewModel.alerts.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){
                binding.imageView.visibility=View.GONE
            }else{
                binding.imageView.visibility=View.VISIBLE
            }
            adapter.alerts=it
            adapter.notifyDataSetChanged()
        })
        binding.fab.setOnClickListener{
            if (!Settings.canDrawOverlays(requireContext())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                Toast.makeText(requireContext(),if(Setting.getLang()=="en")"permission required" else "الاذن مطلوب",Toast.LENGTH_SHORT).show()
                startActivityForResult(intent, 155)
            }else{
                startAdding()
            }
        }
        if(isLocationMapSet)
        {
            val fragmentListOfAlerts=FragmentListOfAlerts()
            fragmentListOfAlerts.isCancelable=false
            fragmentListOfAlerts.viewModel=alertsViewModel
            fragmentListOfAlerts.show(requireActivity().supportFragmentManager,null)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== 155)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                startAdding()
            }
        }else{
            Dialogs.SnakeToast(binding.recyclerView,if(Setting.getLang()=="en")"Permissions Not allowed" else "لم يتم منح الاذن")
        }
    }


    private fun startAdding() {
        if(UserStates.checkConnectionState(requireActivity()))
        {
            val fragmentShowLocationDetector = FragmentLocationDetector()
            fragmentShowLocationDetector.mode = 1
            fragmentShowLocationDetector.isAlert = true
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.my_host_fragment,
                    fragmentShowLocationDetector,
                    null
                )
                .addToBackStack(null)
                .commit()
        }else{
            Dialogs.SnakeToast(binding.root,if(Setting.getLang()=="en")"Please Check Your Connection" else "تأكد من اتصال الانترنت")
        }
    }
    override fun onClick(alert: Alert) {
        val builder= Dialogs.getAletDialogBuilder(binding.root.context,"Alert !!","Do You Want really to Delete Location ?")
        builder.setPositiveButton("No"){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("Yes"){ _, _ ->
            AlarmUtils.canelAlarm(requireContext(),alert)
            alertsViewModel.deleteAlert(alert)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}