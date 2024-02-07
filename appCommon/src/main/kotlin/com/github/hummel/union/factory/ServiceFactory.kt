package com.github.hummel.union.factory

import com.github.hummel.union.service.*
import com.github.hummel.union.service.impl.*

@Suppress("unused", "RedundantSuppression")
object ServiceFactory {
	val loginService: LoginService by lazy { LoginServiceImpl() }
	val botService: BotService by lazy { BotServiceImpl() }
	val userService: UserService by lazy { UserServiceImpl() }
	val adminService: AdminService by lazy { AdminServiceImpl() }
	val ownerService: OwnerService by lazy { OwnerServiceImpl() }
	val dataService: DataService by lazy { DataServiceImpl() }
	val accessService: AccessService by lazy { AccessServiceImpl() }
}