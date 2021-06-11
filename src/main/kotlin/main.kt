fun main() {

    var win = false
    var quit = false

    while (!quit) {

        println("""Bienvenue au MasterMind. Choisissez votre niveau de difficulté :
            |1. Facile (4 valeurs à trouver entre 0 et 5, 20 essais)
            |2. Moyen (4 valeurs à trouver entre 0 et 9, 12 essais). Difficulté par défaut
            |3. Difficile (5 valeurs à trouver entre 0 et 9, 10 essais)
        """.trimMargin())

        val choice = readLine()

        val nbrOfModality = checkModality(choice)
        val nbrOfElement = checkElement(choice)
        val maxNbrOfTry = checkTry(choice)

        val listToFind = generateListOfRandomNumbers(nbrOfElement, nbrOfModality)
        print(listToFind)

        // Début de l'autre boucle
        var nbrOfTry = 0

        while (!win && nbrOfTry < maxNbrOfTry) {
            val listToTest = mutableListOf<Int>()

            println("A vous de deviner !")
            println()
            for (i in 1..nbrOfElement) {
                print("Veuillez insérer l'élément $i de votre proposition : ")
                listToTest.add(readLine()!!.toInt())
            }

            val misplaced = checkMisplaced(listToTest, listToFind)
            val wellplaced = checkWellplaced(listToTest, listToFind)

            println("""Vous avez :
            |   - $misplaced élément(s) mal placé(s)
            |   - $wellplaced élément(s) bien placé(s)
        """.trimMargin())

            win = checkWin(listToTest, listToFind)
            nbrOfTry++
        }

        quit = if (win) {
            println("Bravo, vous avez trouvé tous les éléments (score : ${calculateScore(nbrOfTry, choice)}) ! Voulez-vous recommencer ? (o/n)")
            !checkWantToRestart(readLine())
        } else {
            println("Dommage, vous n'avez pas su trouver les éléments ! Voulez-vous recommencer ? (o/n)")
            !checkWantToRestart(readLine())
        }

        if (!quit) {
            win = false
        }
    }



}

fun generateListOfRandomNumbers(nbrOfElement : Int, nbrOfModality : Int) : MutableList<Int> {

    val result = mutableListOf<Int>()

    for (i in 0 until nbrOfElement) {
        result.add((0 until nbrOfModality).random())
    }

    return result
}

fun checkModality(choice : String?) : Int = if (choice == "1") 6 else 10
fun checkElement(choice: String?) : Int = if (choice == "3") 5 else 4
fun checkTry(choice: String?) : Int = when (choice) {
    "1" -> 20
    "3" -> 10
    else -> 12
}


fun checkMisplaced(listToTest : MutableList<Int>, listToFind : MutableList<Int>) : Int {

    var result = 0

    for ((index, value) in listToTest.withIndex()) {

        val alreadyWellPlaced = value == listToFind[index]

        if (!alreadyWellPlaced) {
            val valueTested = mutableListOf<Int>()
            var i = 0
            while (i < listToFind.size) {
                val valueAlreadyFound = listToTest[i] == listToFind[i]
                val valueAlreadyTested = valueTested.contains(listToFind[i])
                if (value == listToFind[i] && !valueAlreadyFound && !valueAlreadyTested) {
                    result++
                    valueTested.add(listToFind[i])
                }
                i++
            }
        }
    }

    return result

}

fun checkWellplaced(listToTest : MutableList<Int>, listToFind : MutableList<Int>) : Int {

    var result = 0

    for ((index, value) in listToTest.withIndex()) {
        if (value == listToFind[index]) {
            result++
        }
    }

    return result
}

fun checkWin(listToTest : MutableList<Int>, listToFind : MutableList<Int>) : Boolean {
    var result = true

    for ((index, value) in listToTest.withIndex()) {
        if (value != listToFind[index]) {
            result = false
        }
    }

    return result
}

fun checkWantToRestart(choice: String?) : Boolean = choice == "o"

fun calculateScore(nbrOfTry : Int, choice: String?) : Int {
    return when (choice) {
        "1" -> 100/nbrOfTry
        "3" -> 200/nbrOfTry
        else -> 150/nbrOfTry
    }
}