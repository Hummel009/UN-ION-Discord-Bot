package hummel.factory

import hummel.service.*
import hummel.service.impl.*

object ServiceFactory {
	val adminService: AdminService by lazy { AdminServiceImpl() }
	val ownerService: OwnerService by lazy { OwnerServiceImpl() }
	val moderService: ModerService by lazy { ModerServiceImpl() }
	val userService: UserService by lazy { UserServiceImpl() }
	val botService: BotService by lazy { BotServiceImpl() }
	val loginService: LoginService by lazy { LoginServiceImpl() }
	val dataService: DataService by lazy { DataServiceImpl() }
	val cryptService: CryptService by lazy { CryptServiceImpl() }
}