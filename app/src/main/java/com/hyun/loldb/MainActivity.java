package com.hyun.loldb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPrice, etGrade;
    Button btnInit, btnInput, btnSel, btnUp, btnDel;
    ListView lvList;

    LolDB lolDB;//데이터베이스 관리 객체
    SQLiteDatabase sqlDb;//쿼리 실행 객체(jdbc에서 Statement와 같은 역할을 담당)

    MyAdapter mad;
    ArrayList<LolItem> lolItems = new ArrayList<>();
    int selNo = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("롤 아이템 정보");


        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etGrade = findViewById(R.id.etGrade);

        btnInit = findViewById(R.id.btnInit);
        btnInput = findViewById(R.id.btnInput);
        btnSel = findViewById(R.id.btnSel);
        btnUp = findViewById(R.id.btnUp);
        btnDel = findViewById(R.id.btnDel);
        lvList = findViewById(R.id.lvList);

        mad = new MyAdapter(this, lolItems);
        lvList.setAdapter(mad);

        //DB 관련 처리
        lolDB = new LolDB(this);

        //초기화 버튼 처리
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getWritableDatabase() : DB를 읽기/쓰기 모드로 오픈(연결)
                sqlDb = lolDB.getWritableDatabase();
                //Helper 클래스의 onUpgrade() 메소드로 처리
                lolDB.onUpgrade(sqlDb, 1, 2);
                //DB 연결 해제
                sqlDb.close();

                btnSel.callOnClick();
            }
        });//btnInit end

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                String grade = etGrade.getText().toString();

                if(name.equals("") || price.equals("") || grade.equals("")){
                    Toast.makeText(getApplicationContext(), "입력하세요!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //PreparedStatement와 유사한 객체
                ContentValues values = new ContentValues();
                values.put("lName", name);
                int num = Integer.parseInt(price);
                values.put("lPrice", num);
                values.put("lGrade", grade);

                sqlDb = lolDB.getWritableDatabase();

                long result = sqlDb.insert("groupTbl", null, values);

                //쿼리문(String)을 작성하여 다음과 같이 실행할 수도 있음.
                //INSERT INTO groupTbl VALUES ('BTS', 7)
                //String query = "INSERT INTO groupTbl VALUES ('" + name
                //        + "', " + number + ")";
                //sqlDb.execSQL(query);

                sqlDb.close();

                if(result == -1){
                    Toast.makeText(getApplicationContext(), "입력 실패",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "입력 성공",
                            Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etPrice.setText("");
                    etGrade.setText("");
                    //입력 성공 시 조회 버튼의 onClick 메소드를 호출
                    btnSel.callOnClick();
                }
            }
        });//btnInput end

        btnSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb = lolDB.getReadableDatabase();
                Cursor cur;
                cur = sqlDb.rawQuery("SELECT * FROM groupTbl", null);

                lolItems.clear();

                while (cur.moveToNext()){
                    LolItem lol = new LolItem(cur.getInt(0),cur.getString(1),
                            cur.getString(2), cur.getString(3));
                    lolItems.add(lol);
                }

                mad.notifyDataSetChanged();

                //메모리 해제 및 연결 해제
                cur.close();
                sqlDb.close();
            }
        });//btnSel end

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();//where절에서 사용
                String price = etPrice.getText().toString();//변경할 값
                String grade = etGrade.getText().toString();//변경할 값

                if(name.equals("") || price.equals("") || grade.equals("")){
                    Toast.makeText(getApplicationContext(), "선택하세요!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int num = Integer.parseInt(price);

                ContentValues values = new ContentValues();
                values.put("lName", name);
                values.put("lPrice", num);
                values.put("lGrade", grade);

                //UPDATE groupTbl SET gNumber = 6 WHERE gName='BTS'
                //DB를 쓰기 모드로 연결
                sqlDb = lolDB.getWritableDatabase();
                int result = sqlDb.update("groupTbl", values,
                        "lNo=?", new String[]{String.valueOf(selNo)});

                sqlDb.close();

                if(result == 0){
                    Toast.makeText(getApplicationContext(), "수정실패",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "수정성공",
                            Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etPrice.setText("");
                    etGrade.setText("");
                    btnSel.callOnClick();
                }
            }
        });//btnUp end

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if(name.equals("")){
                    Toast.makeText(getApplicationContext(), "선택하세요!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                sqlDb = lolDB.getWritableDatabase();

                int result = sqlDb.delete("groupTbl", "lNo=?",
                        new String[]{String.valueOf(selNo)});

                sqlDb.close();

                if(result == 0){
                    //실패 메시지 출력
                    Toast.makeText(getApplicationContext(), "삭제실패",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    //성공 메시지 출력
                    Toast.makeText(getApplicationContext(), "삭제성공",
                            Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etPrice.setText("");
                    etGrade.setText("");
                    btnSel.callOnClick();
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LolItem lol = lolItems.get(position);
                etName.setText(lol.getItemname());
                etPrice.setText("" + lol.getPrice());
                etGrade.setText(lol.getGrade());
                selNo = lol.getNo();
            }
        });



    }//onCreate end
}//class end