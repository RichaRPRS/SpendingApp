package com.rprs.admin.estimateonly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    Context context;

    private DatabaseAccess(Context context)
    {
        this.context=context;
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /*** Open the database connection.*/
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /*** Close the database connection.*/
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<HashMap<String,String>> getQuotes() {
        List<HashMap<String,String>> listm = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Category", null);
        cursor.moveToFirst();
        HashMap<String,String> hashMap;
        while (!cursor.isAfterLast()) {
            hashMap=new HashMap<>();
            hashMap.put("id",cursor.getString(0));
            hashMap.put("name",cursor.getString(1));
            hashMap.put("image",cursor.getString(2));
            listm.add(hashMap);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return listm;
    }

    public List<HashMap<String,String>> getAmounts(int date,int years) {
        List<HashMap<String,String>> listm = new ArrayList<>();
        String query="select sum(a.amount) as sums,pay_type from 'transaction' as a inner join Payment_type as b\n" +
                "on a.pay_id=b.pay_id where a.month='"+date+"' and a.years='"+years+"' group by a.pay_id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        HashMap<String,String> hashMap;
        while (!cursor.isAfterLast()) {
            hashMap=new HashMap<>();
            hashMap.put("amount",cursor.getString(0));
            hashMap.put("type",cursor.getString(1));
            listm.add(hashMap);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return listm;
    }

    public List<HashMap<String,String>> getAmountsWeek(int week,int years) {
        List<HashMap<String,String>> listm = new ArrayList<>();
        String query="select sum(a.amount) as sums,pay_type from 'transaction' as a inner join Payment_type as b\n" +
                "on a.pay_id=b.pay_id where a.week='"+week+"' and a.years='"+years+"' group by a.pay_id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        HashMap<String,String> hashMap;
        while (!cursor.isAfterLast()) {
            hashMap=new HashMap<>();
            hashMap.put("amount",cursor.getString(0));
            hashMap.put("type",cursor.getString(1));
            listm.add(hashMap);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return listm;
    }

    public List<HashMap<String,String>> getAmountsYear(int year) {
        List<HashMap<String,String>> listm = new ArrayList<>();
        String query="select sum(a.amount) as sums,pay_type from 'transaction' as a inner join Payment_type as b\n" +
                "on a.pay_id=b.pay_id where a.years='"+year+"' group by a.pay_id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        HashMap<String,String> hashMap;
        while (!cursor.isAfterLast()) {
            hashMap=new HashMap<>();
            hashMap.put("amount",cursor.getString(0));
            hashMap.put("type",cursor.getString(1));
            listm.add(hashMap);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return listm;
    }

    public void insertfun(String values){
        database.execSQL("INSERT INTO Category VALUES('"+values+"');");
    }

    public void insertCategory(ContentValues cv){
        database.insert("Category",null,cv);
    }

    public void updatecategory(ContentValues cv,String id){
        database.update("Category", cv, "categ_id='"+id+"'",null);
        //database.execSQL("Update Category set image=('"+img+"') where categ_id='1';");
    }

    public void deleteCategory(String id){
        database.delete("Category","categ_id='"+id+"'",null);
    }

    public void insertTran(int amount,String date,int categ_id,String note,int pay_id1,int day,int week,int month,int year){
        database.execSQL("INSERT INTO 'Transaction'(amount,date,categ_id,note,pay_id,day,week,month,years) " +
                "VALUES("+amount+",'"+date+"',"+categ_id+",'"+note+"',"+pay_id1+","+day+","+week+","+month+","+year+");");
    }

    public void updatetrans(int amount,String date,int categ_id,String note,int pay_id1,String id,int day,int week,int month,int year){
        String query="update 'Transaction' set amount="+amount+",date='"+date
                +"',categ_id='"+categ_id+"',note='"+note+"',pay_id='"+pay_id1+"',day='"+day+"',week='"+week+"',month='"+month+"',years='"+year+"' where tran_id='"+id+"'";
        database.execSQL(query);
    }

    public void deleteTrans(String id){
        //database.delete("","tran_id='"+id+"'",null);
        String query="delete from 'Transaction' where tran_id='"+id+"'";
        database.execSQL(query);
    }

    public ArrayList<TransModel> getTransaction(int dates,int years){
        //List<String> list = new ArrayList<>();
        ArrayList<TransModel> dataModels= new ArrayList<>();
        String query="SELECT a.tran_id,a.amount,a.date,b.categ_id,b.categ_name,a.note,a.years,c.pay_type,b.image FROM 'Transaction' as a inner join Category as b \n" +
                "on a.categ_id=b.categ_id inner join Payment_type as c on a.pay_id=c.pay_id where a.month='"+dates+"' and a.years='"+years+"' order by a.date desc";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tran_id=cursor.getString(0);
            int amount=cursor.getInt(1);
            String date=cursor.getString(2);
            String categ_id=cursor.getString(3);
            String categ_name=cursor.getString(4);
            String note=cursor.getString(5);
            String year=cursor.getString(6);
            String pay_id=cursor.getString(7);
            byte[] image=cursor.getBlob(8);
            TransModel model=new TransModel(tran_id,amount,categ_id,categ_name,pay_id,date,note,image);
            dataModels.add(model);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return dataModels;
    }

    public ArrayList<TransModel> getTransactionWeek(int week,int years){
        //List<String> list = new ArrayList<>();
        ArrayList<TransModel> dataModels= new ArrayList<>();
        String query="SELECT a.tran_id,a.amount,a.date,b.categ_id,b.categ_name,a.note,a.years,c.pay_type,b.image FROM 'Transaction' as a inner join Category as b \n" +
                "on a.categ_id=b.categ_id inner join Payment_type as c on a.pay_id=c.pay_id where a.week='"+week+"' and a.years='"+years+"'  order by a.date desc";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tran_id=cursor.getString(0);
            int amount=cursor.getInt(1);
            String date=cursor.getString(2);
            String categ_id=cursor.getString(3);
            String categ_name=cursor.getString(4);
            String note=cursor.getString(5);
            String year=cursor.getString(6);
            String pay_id=cursor.getString(7);
            byte[] image=cursor.getBlob(8);
            TransModel model=new TransModel(tran_id,amount,categ_id,categ_name,pay_id,date,note,image);
            dataModels.add(model);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return dataModels;
    }

    public ArrayList<TransModel> getTransactionYear(int years){
        //List<String> list = new ArrayList<>();
        ArrayList<TransModel> dataModels= new ArrayList<>();
        String query="SELECT a.tran_id,a.amount,a.date,b.categ_id,b.categ_name,a.note,a.years,c.pay_type,b.image FROM 'Transaction' as a inner join Category as b \n" +
                "on a.categ_id=b.categ_id inner join Payment_type as c on a.pay_id=c.pay_id where a.years='"+years+"'  order by a.date desc";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tran_id=cursor.getString(0);
            int amount=cursor.getInt(1);
            String date=cursor.getString(2);
            String categ_id=cursor.getString(3);
            String categ_name=cursor.getString(4);
            String note=cursor.getString(5);
            String year=cursor.getString(6);
            String pay_id=cursor.getString(7);
            byte[] image=cursor.getBlob(8);
            TransModel model=new TransModel(tran_id,amount,categ_id,categ_name,pay_id,date,note,image);
            dataModels.add(model);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return dataModels;
    }

    public ArrayList<ExcatModel> getCategory() {
        //List<HashMap<String,String>> listm = new ArrayList<>();
        ArrayList<ExcatModel> dataModels= new ArrayList<>();
        String query="select a.*,b.pay_type from Category as a inner join\n" +
                " Payment_type as b on a.pay_id=b.pay_id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id=cursor.getString(0);
            String name=cursor.getString(1);
            byte[] image=cursor.getBlob(2);
            String paytype=cursor.getString(4);
            ExcatModel excatModel=new ExcatModel(id,name,paytype,image);
            dataModels.add(excatModel);
            cursor.moveToNext();
        }
        cursor.close();
        return dataModels;
    }

    public ArrayList<ExcatModel> getCategory(String ids) {
        //List<HashMap<String,String>> listm = new ArrayList<>();
        ArrayList<ExcatModel> dataModels= new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Category where categ_id='"+ids+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id=cursor.getString(0);
            String name=cursor.getString(1);
            byte[] image=cursor.getBlob(2);
            ExcatModel excatModel=new ExcatModel(id,name,image);
            dataModels.add(excatModel);
            cursor.moveToNext();
        }
        cursor.close();
        return dataModels;
    }

    public List<String> getAllCategory() {
       List<String> arr=new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Category", null);
        cursor.moveToFirst();
        arr.add("All Category");
        int i=0;
        while (!cursor.isAfterLast()) {
            String id=cursor.getString(0);
            String name=cursor.getString(1);
            byte[] image=cursor.getBlob(2);
            arr.add(name);
            cursor.moveToNext();
            i=i++;
        }
        cursor.close();
        return arr;
    }

    public void insertuser(String name,String mobile){
        database.execSQL("INSERT INTO Profile (name,mobile) VALUES('"+name+"','"+mobile+"');");
    }

    public void updateuser(String name,String mobile,String user_id){
        database.execSQL("UPDATE Profile SET name='"+name+"',mobile='"+mobile+"' WHERE Profile_id='"+user_id+"'");
    }

    public void deleteUser(String id){
        database.delete("Profile","Profile_id='"+id+"'",null);
    }

    public List<String> getAlluser() {
        List<String> arr=new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Profile", null);
        cursor.moveToFirst();
        int i=0;
        while (!cursor.isAfterLast()) {
            String id=cursor.getString(0);
            String name=cursor.getString(1);
            String mobile=cursor.getString(2);
            arr.add(name);
            cursor.moveToNext();
            i=i++;
        }
        cursor.close();
        return arr;
    }

    public List<HashMap<String,String>> getuser() {
        List<HashMap<String,String>> listm = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Profile", null);
        cursor.moveToFirst();
        HashMap<String,String> hashMap;
        while (!cursor.isAfterLast()) {
            hashMap=new HashMap<>();
            hashMap.put("id",cursor.getString(0));
            hashMap.put("name",cursor.getString(1));
            hashMap.put("mobile",cursor.getString(2));
            listm.add(hashMap);
            //list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return listm;
    }
}
