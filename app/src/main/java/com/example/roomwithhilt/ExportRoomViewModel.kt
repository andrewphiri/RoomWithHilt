package com.example.roomwithhilt

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.example.roomwithhilt.data.FruitRepository
import com.example.roomwithhilt.data.Fruits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Field
import javax.inject.Inject

@HiltViewModel
class ExportRoomViewModel @Inject constructor(
    private val repository: FruitRepository,
    private val baseApplication: FruitApplication
) : ViewModel() {

    //Export to excel
    suspend fun exportToExcel() {
        try {
            val workbook = XSSFWorkbook()
            val folder = File(baseApplication.filesDir, "exported_files")
            //check if folder exists
            if (!folder.exists()) {
                folder.mkdirs()
            }
            exportFruits(workbook)
            val file = File(folder, "fruits.xlsx")
            try {
                //write workbook to file
                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use { outputStream ->
                        workbook.write(outputStream)
                    }

                    //Get file uri
                    val fileUri = FileProvider.getUriForFile(
                        baseApplication.applicationContext,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        file
                    )

                    //Open file
                    val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    //Share file
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        putExtra(Intent.EXTRA_STREAM, fileUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    val chooserIntent =
                        Intent.createChooser(
                            shareIntent,
                            "Open or Share File"
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


                    // Add the viewIntent as an extra option
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(viewIntent))

                    baseApplication.startActivity(chooserIntent)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun exportFruits(workbook: XSSFWorkbook) {
        val sheet = workbook.createSheet("Fruits")

        //Fields you want to export
        val preferredFruitsFields = arrayOf(
            "name",
            "type",
            "date"
        )

        //All fields in Fruits class
        val allFruitsFields = Fruits::class.java.declaredFields
        //Filter only the fields you want to export
        val fruitFieldsToExport = arrayOfNulls<Field>(preferredFruitsFields.size)

        for (i in preferredFruitsFields.indices) {
            for (field in allFruitsFields) {
                if (field.name == preferredFruitsFields[i]) {
                    field.isAccessible = true // make private field accessible
                    fruitFieldsToExport[i] = field
                    break
                }
            }
        }

        // Add header row
        val headerRow = sheet.createRow(0)
        val headerStyle = createHeaderStyle(workbook)
        for (i in fruitFieldsToExport.indices) {
            val cell = headerRow.createCell(i)
            cell.setCellValue(fruitFieldsToExport[i]?.name)
            cell.cellStyle = headerStyle
        }

        //fetch data
        val fruits = repository.getAllFruits().first()
        var rowNum = 1
        for (fruit in fruits) {
            val row = sheet.createRow(rowNum++)
            for (i in fruitFieldsToExport.indices) {
                try {
                    // Access the field using reflection
                    val value = fruitFieldsToExport[i]?.get(fruit)
                    row.createCell(i).setCellValue(value.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    fun createHeaderStyle(workbook: XSSFWorkbook): XSSFCellStyle {
        // Create a new cell style for the header
        val headerStyle: XSSFCellStyle = workbook.createCellStyle()

        // Set background color
        headerStyle.fillForegroundColor = IndexedColors.LIGHT_BLUE.index
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        // Set font color and make it bold
        val font: XSSFFont = workbook.createFont()
        font.bold = true
        font.color = IndexedColors.WHITE.index
        headerStyle.setFont(font)

        // Set alignment
        headerStyle.alignment = HorizontalAlignment.CENTER
        headerStyle.verticalAlignment = VerticalAlignment.CENTER

        return headerStyle
    }
}