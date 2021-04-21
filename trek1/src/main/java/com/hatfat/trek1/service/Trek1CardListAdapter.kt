package com.hatfat.trek1.service

import com.hatfat.trek1.data.Trek1Card
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek1CardListAdapter @Inject constructor() {

    private var nextId: Int = 0

    fun convert(inputStream: InputStream, isVirtual: Boolean): List<Trek1Card> {
        val cards = mutableListOf<Trek1Card>()

        val reader = BufferedReader(InputStreamReader(inputStream))

        val firstLine = reader.readLine()
        val lines = firstLine.split("\t")
        if (lines.size != 28) {
            throw RuntimeException("Invalid card format.")
        }

        reader.forEachLine {
            val line = it.split("\t")
            cards.add(
                Trek1Card(
                    line[0],
                    line[1],
                    line[2],
                    line[3],
                    line[4],
                    line[5],
                    line[6],
                    line[7],
                    line[8],
                    line[9],
                    line[10],
                    line[11],
                    line[12],
                    line[13],
                    line[14],
                    line[15],
                    line[16],
                    line[17],
                    line[18],
                    line[19],
                    line[20],
                    line[21],
                    line[22],
                    line[23],
                    line[24],
                    line[25],
                    line[26],
                    line[27],
                    isVirtual,
                    nextId++
                )
            )
        }

        return cards
    }
}