package com.g.pdfcreate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText number;
    private EditText c_amount;
    private EditText d_amount;
    TextView date_picker;
    private Button submit, pdf;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        c_amount = findViewById(R.id.c_amount);
        d_amount = findViewById(R.id.d_amount);
        date_picker = findViewById(R.id.date_picker);
        submit = findViewById(R.id.submit);
        pdf = findViewById(R.id.pdf);

        dbHandler = new DBHandler(this);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Here, we create a Calendar object and set it to the selected date.
                                Calendar selectedCalendar = Calendar.getInstance();
                                selectedCalendar.set(year, monthOfYear, dayOfMonth);

                                // Now, we get the current date and time.
                                Calendar currentCalendar = Calendar.getInstance();

                                // Set the time components from the current time.
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, currentCalendar.get(Calendar.HOUR_OF_DAY));
                                selectedCalendar.set(Calendar.MINUTE, currentCalendar.get(Calendar.MINUTE));
                                selectedCalendar.set(Calendar.SECOND, currentCalendar.get(Calendar.SECOND));

                                // Format the selected date and current time.
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String formattedDateTime = dateFormat.format(selectedCalendar.getTime());

                                // Now, you can store the formattedDateTime in your SQLite database.
                                // Assuming dbHelper is an instance of your database helper class.
                               // DBHandler.addUser(name, number, c_amount, d_amount, formattedDateTime);
                                date_picker.setText(formattedDateTime);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });
        display();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // below line is to get data from all edit text fields.
                String Name = name.getText().toString();
                String Number = number.getText().toString();
                String C_amount = c_amount.getText().toString();
                String D_amount = d_amount.getText().toString();
                String Date_picker = date_picker.getText().toString();


                // validating if the text fields are empty or not.
                if (Name.isEmpty() || Number.isEmpty() || C_amount.isEmpty() || D_amount.isEmpty()|| Date_picker.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Boolean data = dbHandler.addUser(Name, Number, C_amount, D_amount,Date_picker);
                    if (data == false) {
                        Toast.makeText(MainActivity.this, "Something is wrong in Database.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "data has been added.", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        number.setText("");
                        c_amount.setText("");
                        d_amount.setText("");
                        date_picker.setText("");
                    }
                }display();
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase myDB;
                myDB = dbHandler.getReadableDatabase();

                Cursor cursor = myDB.query("user_details", null, null, null, null, null, null);

                // Create a new PDF document
                Document document = new Document();

                try {
                    // Specify the file path for the PDF
                    File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
                    if (!folder.exists()) {
                        folder.createNewFile();
                    }
                    //File file = new File(folder, "my_table.pdf");
                    File file = File.createTempFile("Udhar_Book", ".pdf", folder);

                    // Create a new PDF writer
                    PdfWriter.getInstance(document, new FileOutputStream(file));

                    // Open the document
                    document.open();

                    final Font FONTBU = new Font(Font.FontFamily.HELVETICA, 17, Font.BOLD);
                    final Font FONTN = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);


                    // Add the main header
                    Paragraph mainHeader = new Paragraph("Udhar Book Statement", FONTBU);
                    mainHeader.setAlignment(Element.ALIGN_CENTER);
                    document.add(mainHeader);

                    // Add current date and time
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Paragraph dateTime = new Paragraph("Generated on: " + dateFormat.format(currentDate), FONTN);
                    dateTime.setAlignment(Element.ALIGN_CENTER);
                    document.add(dateTime);

                    // Add space after the date and time
                    document.add(new Paragraph(" "));
                    
                    // Create a new table with 5 columns
                    PdfPTable table = new PdfPTable(6);
                    table.setTotalWidth(PageSize.A4.getWidth());

//                    PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf("Udhar Book Statment"), FONTBU));
//                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    c1.setColspan(5);


                    // Add column headers to the table
                    table.addCell(new PdfPCell(new Phrase("Name")));
                    table.addCell(new PdfPCell(new Phrase("Number")));
                    table.addCell(new PdfPCell(new Phrase("Credit")));
                    table.addCell(new PdfPCell(new Phrase("Debit")));
                    table.addCell(new PdfPCell(new Phrase("NetBalens")));
                    table.addCell(new PdfPCell(new Phrase("Dtate time")));

                    // Add rows to the table
                    while (cursor.moveToNext()) {
                        table.addCell(new PdfPCell(new Phrase(cursor.getString(0))));
                        table.addCell(new PdfPCell(new Phrase(cursor.getString(1))));
                        table.addCell(new PdfPCell(new Phrase(cursor.getString(2))));
                        table.addCell(new PdfPCell(new Phrase(cursor.getString(3))));




                        // Assuming cursor.getString(2) corresponds to debit (column 3)
                        String credit = cursor.getString(2);
                        String debit = cursor.getString(3);

                        // Perform subtraction and add the result to the table
                        double creditValue = Double.parseDouble(credit);
                        double debitValue = Double.parseDouble(debit);
                        double result = creditValue - debitValue;

                        table.addCell(new PdfPCell(new Phrase(String.valueOf(result))));
                        table.addCell(new PdfPCell(new Phrase(cursor.getString(4))));
                    }

                    // Add the table to the document
                    document.add(table);

                    Toast.makeText(getApplicationContext(), "PDF generated successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Close the document
                    document.close();
                    cursor.close();
                }

            }
        });
    }
    @SuppressLint("Range")
    private void display() {
        ArrayList<String> name,number,credit,debit;
        MyAdapter adapter;
        RecyclerView recyclerView;
        name=new ArrayList<>();
        number=new ArrayList<>();
        credit=new ArrayList<>();
        debit=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);

        adapter=new MyAdapter(this,name,number,credit,debit);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor1=dbHandler.readData();
        if (cursor1.getCount()==0){
            Toast.makeText(this,"No Entry Exist",Toast.LENGTH_SHORT).show();
        }else {
            while (cursor1.moveToNext()){
                name.add(String.valueOf(cursor1.getString(0)));
                number.add(String.valueOf(cursor1.getString(1)));
                credit.add(String.valueOf(cursor1.getString(2)));
                debit.add(String.valueOf(cursor1.getString(3)));
            }
            // Notify the adapter about the data change
            adapter.notifyDataSetChanged();
        }
    }

}