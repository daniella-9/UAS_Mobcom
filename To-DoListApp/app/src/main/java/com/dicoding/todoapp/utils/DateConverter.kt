import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateConverter {
    private const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"

    fun convertMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return sdf.format(calendar.time)
    }

    fun convertStringToMillis(dateTimeString: String): Long {
        val sdf = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        try {
            val date = sdf.parse(dateTimeString)
            return date?.time ?: 0L
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0L
    }
}
