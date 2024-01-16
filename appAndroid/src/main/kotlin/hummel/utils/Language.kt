package hummel.utils

import hummel.bean.ServerData
import java.time.Month

enum class Lang(private val ru: String, private val en: String) {
	NO_CONNECTION("Сайт с нейросетью временно недоступен.", "The neural network site is temporarily unavailable."),
	EXIT("Бот будет выключен.", "The bot will be turned off."),
	NUKE("Сообщения были удалены.", "Messages were removed."),
	RANDOM("Случайное значение", "Random value"),
	NO_BIRTHDAYS("Дней рождения нет.", "No birthdays."),
	HAPPY_BIRTHDAY("с днём рождения", "happy birthday"),
	NO_ACCESS("У вас нет доступа к этой команде.", "You have no access to this command."),
	IMPORT("Данные импортированы.", "Data was imported."),
	INVALID_ARG("Недопустимые аргументы.", "Invalid arguments provided."),
	INVALID_FORMAT("Недопустимый формат аргумента.", "Invalid argument format."),
	SET_LANGUAGE("Установлен язык", "Set language"),
	CURRENT_CHANCE("Текущий шанс сообщения", "Current chance"),
	CURRENT_LANG("Текущий язык интерфейса", "Current language"),
	SET_CHANCE("Установлен шанс сообщения", "Set chance"),
	ADDED_MANAGER("Добавлена управляющая роль", "Added manager role"),
	ADDED_CHANNEL("Добавлен секретный канал", "Added secret channel"),
	ADDED_BIRTHDAY("Добавлен день рождения", "Added birthday"),
	REMOVED_BIRTHDAY("Удалён день рождения", "Removed birthday"),
	REMOVED_MANAGER("Удалена управляющая роль", "Removed manager role"),
	REMOVED_CHANNEL("Удалён секретный канал", "Removed secret channel"),
	CLEARED_CHANNELS("Секретные каналы сервера очищены.", "Server secret channels cleared."),
	CLEARED_MANAGERS("Управляющие роли сервера очищены.", "Server manager roles cleared."),
	CLEARED_DATA("Данные сервера очищены.", "Server serverData cleared."),
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

	operator fun get(serverData: ServerData): String = if (serverData.lang == "ru") ru else en
}

fun getFormattedTranslatedDate(month: Month, serverData: ServerData, day: Int): String {
	return when (month) {
		Month.JANUARY -> if (serverData.lang == "ru") "$day января" else "January $day"
		Month.FEBRUARY -> if (serverData.lang == "ru") "$day февраля" else "February $day"
		Month.MARCH -> if (serverData.lang == "ru") "$day марта" else "March $day"
		Month.APRIL -> if (serverData.lang == "ru") "$day апреля" else "April $day"
		Month.MAY -> if (serverData.lang == "ru") "$day мая" else "May $day"
		Month.JUNE -> if (serverData.lang == "ru") "$day июня" else "June $day"
		Month.JULY -> if (serverData.lang == "ru") "$day июля" else "July $day"
		Month.AUGUST -> if (serverData.lang == "ru") "$day августа" else "August $day"
		Month.SEPTEMBER -> if (serverData.lang == "ru") "$day сентября" else "September $day"
		Month.OCTOBER -> if (serverData.lang == "ru") "$day октября" else "October $day"
		Month.NOVEMBER -> if (serverData.lang == "ru") "$day ноября" else "November $day"
		Month.DECEMBER -> if (serverData.lang == "ru") "$day декабря" else "December $day"
	}
}