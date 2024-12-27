package com.example.jollycat

import org.json.JSONArray
import org.json.JSONObject

class JsonParser {
    companion object {
        fun parseCatData(response: JSONArray): List<Cat> {
            val catList = mutableListOf<Cat>()

            for (i in 0 until response.length()) {
                val catObject = response.getJSONObject(i)
                val catID = catObject.getString("CatID") ?: ""
                val catName = catObject.getString("CatName") ?: ""
                val catImage = catObject.getString("CatImage") ?: ""
                val catPrice = catObject.getInt("CatPrice")
                val catDescription = catObject.getString("CatDescription") ?: ""

                val cat = Cat(catID, catName, catImage, catPrice, catDescription)
                catList.add(cat)
            }

            return catList
        }
    }
}
