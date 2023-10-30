package hummel.utils

import hummel.structures.ServerData

enum class Lang(private val ru: String, private val en: String) {
	NO_CONNECTION("Сайт с нейросетью временно недоступен.", "The neural network site is temporarily unavailable."),
	EXIT("Бот будет выключен.", "The bot will be turned off."),
	SHUTDOWN("Хост-компьютер будет выключен.", "The host computer will be shut down."),
	NUKE("Сообщения были удалены.", "Messages were removed."),
	RANDOM("Случайное значение", "Random value"),
	BACKUP_ERROR("Произошла ошибка при копировании и отправке файла.", "Error when copying and sending a file."),
	HAPPY_BIRTHDAY("с днём рождения", "happy birthday"),
	NO_ACCESS("У вас нет доступа к этой команде.", "You have no access to this command."),
	INVALID_ARG("Недопустимые аргументы.", "Invalid arguments provided."),
	INVALID_FORMAT("Недопустимый формат аргумента.", "Invalid argument format."),
	SET_LANGUAGE("Установлен язык", "Set language"),
	SET_CHANCE("Установлен шанс сообщения", "Set chance"),
	ADDED_MANAGER("Добавлена управляющая роль", "Added manager role"),
	ADDED_BIRTHDAY("Добавлен день рождения", "Added birthday"),
	REMOVED_BIRTHDAY("Удалён день рождения", "Removed birthday"),
	REMOVED_MANAGER("Удалена управляющая роль", "Removed manager role"),
	CLEARED_MANAGERS("Управляющие роли сервера очищены.", "Server manager roles cleared."),
	CLEARED_MESSAGES("Сообщения сервера очищены.", "Server messages cleared."),
	CLEARED_BIRTHDAYS("Дни рождения сервера очищены.", "Server birthdays cleared."),
	GAME_YES_1("Да.", "Yes."),
	GAME_YES_2("Можешь не сомневаться в этом).", "You can rest assured of that)."),
	GAME_YES_3("Определённо да!", "Definitely yes!"),
	GAME_YES_4("Я уверен в этом на все 100%", "I'm 100% sure of it."),
	GAME_NO_1("Нет.", "No."),
	GAME_NO_2("Я считаю, что нет)", "I think not."),
	GAME_NO_3("Определённо нет!", "Definitely no!"),
	GAME_NO_4("Точно нет, я уверен на 100%", "Definitely not, I'm 100% sure."),
	MSG_ACCESS("Доступ", "Access"),
	MSG_ERROR("Ошибка", "Error"),
	MSG_SUCCESS("Успех", "Success");

	fun get(data: ServerData): String = if (data.lang == "ru") ru else en
}