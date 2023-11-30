package hummel.factory

import hummel.service.*
import hummel.service.impl.*

object ServiceFactory {
	@Suppress("unused")
	val loginService: LoginService by lazy { LoginServiceImpl() }
	val botService: BotService by lazy { BotServiceImpl() }
	val userService: UserService by lazy { UserServiceImpl() }
	val adminService: AdminService by lazy { AdminServiceImpl() }
	val ownerService: OwnerService by lazy { OwnerServiceImpl() }
	val dataService: DataService by lazy { DataServiceImpl() }
}