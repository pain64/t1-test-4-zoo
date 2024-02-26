import java.text.SimpleDateFormat
import java.util.*

class Food(val id: Long, val name: String, val weight: Float?, val calories: Float?)
class Doctor(val id: Long, val name: String, val education: String?, val specialization: String?)
class Room(val id: Long, val number: String, val cleaningStartH: Int, val cleaningEndH: Int)
class Animal(val id: Long, val name: String?, val type: String, val weight: Float?)

class Location(val roomId: Long, val animalId: Long, val startDate: Date, val endDate: Date)
class Feeding(val animalId: Long, val foodId: Long, val count: Int, val date: Date)
class Healing(val doctorId: Long?, val animalId: Long, val date: Date)

val dateFormat = SimpleDateFormat("DD.MM.YYYY")

/*
  1. Есть ли в зоопарке медведь по кличке "Бугор", весом не менее 400 кг, занимавшийся самолечением
  в один из дней периода 20.05.2023 .. 29.06.2023
 */

fun problem1(animals: List<Animal>, heals: List<Healing>): Boolean =
    animals.find {
        it.name == "Бугор" &&
                it.type == "медведь" &&
                it.weight != null && it.weight > 400.0
    }?.let { bear ->
        heals.any {
            it.doctorId == null && it.animalId == bear.id &&
                    it.date > dateFormat.parse("20.05.2023") &&
                    it.date < dateFormat.parse("29.06.2023")
        }
    } ?: false

/*
  2. Найдите суммарный вес еды, съеденной зверями из вольера номер 6 за 20.05.2023. В случае, если
  вес еды не указан, взять вес равный 1.0кг. Ответ дать в типе Double
 */
fun problem2(locations: List<Location>, feeds: List<Feeding>, food: List<Food>): Double =
    feeds.filter { feed ->
        locations.any { it.roomId == 6L && it.animalId == feed.animalId }
    }.sumOf { feed ->
        food.find { it.id == feed.foodId }!!.weight?.toDouble() ?: 1.0
    }

/*
 3. Сопоставьте каждому зверю, больному ожирением (вес больше 120кг и он при этом не медведь),
 запись на прием к доктору профильной специальности 'ожиролог', имеющему образование в 'ОмГМУ'
 на 20.05.1993. Если врачи уже заняты в данный день, или врачей не хватает, то сопоставьте null.
 Ответ дать в виде Map<Animal, Doctor?>
 */

fun problem3(
    animals: List<Animal>,
    doctors: List<Doctor>,
    heals: List<Healing>
): Map<Animal, Doctor?> {

    val freeDoctors = doctors.filter { d ->
        d.education == "ОмГМУ" && d.specialization == "ожиролог" &&
                !heals.any {
                    it.doctorId == d.id && it.date == dateFormat.parse("20.05.2023")
                }
    }

    val illAnimals = animals.filter {
        (it.weight!!) > 120 && it.type != "медведь"
    }

    var doctorIdx = 0
    return illAnimals.associateWith {
        if (doctorIdx < freeDoctors.lastIndex)
            null
        else
            freeDoctors[doctorIdx].also {
                doctorIdx++
            }
    }
}
