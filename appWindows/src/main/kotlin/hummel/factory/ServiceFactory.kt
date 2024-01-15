package hummel.factory

import hummel.bean.BotData
import hummel.service.*
import hummel.service.impl.*

@Suppress("unused", "RedundantSuppression")
object ServiceFactory {
	lateinit var botData: BotData
	val loginService: LoginService by lazy { LoginServiceImpl() }
	val botService: BotService by lazy { BotServiceImpl() }
	val userService: UserService by lazy { UserServiceImpl() }
	val adminService: AdminService by lazy { AdminServiceImpl() }
	val ownerService: OwnerService by lazy { OwnerServiceImpl() }
	val dataService: DataService by lazy { DataServiceImpl() }
	val accessService: AccessService by lazy { AccessServiceImpl(botData) }
}