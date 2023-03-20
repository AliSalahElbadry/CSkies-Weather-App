package com.app.our.cskies.Repository

import com.app.our.cskies.dp.LocalDataSource
import com.app.our.cskies.network.RemoteSource

interface RepositoryInterface:RemoteSource,LocalDataSource