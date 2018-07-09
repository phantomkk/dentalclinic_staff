package com.dentalclinic.capstone.admin.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dentalclinic.capstone.admin.models.District;
import com.dentalclinic.capstone.admin.models.District;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 15/10/2017.
 */

public class DistrictDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "DentalClinicUser";

    public DistrictDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DistrictDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //name table
    private static final String TABLE_DISTRICT = "Districts";
    private static final String COLUMN_DISTRICT_ID = "id";
    private static final String COLUMN_DISTRICT_NAME = "name";
    private static final String COLUMN_DISTRICT_CITY_ID = "city_id";


    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_DISTRICT + "("
                + COLUMN_DISTRICT_ID + " INTEGER,"
                + COLUMN_DISTRICT_NAME + " TEXT,"
                + COLUMN_DISTRICT_CITY_ID + " INTEGER"
                + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
        // Và tạo lại.
        onCreate(db);
    }
//
//    public void createDefaultToDoIfNeed()  {
//        History history = new History("haha","2143244325435");
//        History history2 = new History("haha","32435435");
//        this.addHistory(history);
//        this.addHistory(history2);
//    }


    public ArrayList<District> getAllDistrict() {
        // Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        ArrayList<District> districts = new ArrayList<District>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DISTRICT + " ORDER BY " + COLUMN_DISTRICT_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                District district = new District();
                district.setId(cursor.getInt(0));
                district.setName(cursor.getString(1));
                district.setCityId(cursor.getInt(2));
                // Thêm vào danh sách.
                districts.add(district);
            } while (cursor.moveToNext());
        }

        // return note list
        return districts;
    }

//    public ArrayList<District> getDistrictOfCity(int cityId) {
//        // Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );
//
//        ArrayList<District> districts = new ArrayList<District>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_DISTRICT +" WHERE "+COLUMN_DISTRICT_ID+" = "+cityId+" ORDER BY "+ COLUMN_DISTRICT_ID +" DESC";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//        // Duyệt trên con trỏ, và thêm vào danh sách.
//        if (cursor.moveToFirst()) {
//            do {
//                District district = new District();
//                district.setId(cursor.getInt(0));
//                district.setName(cursor.getString(1));
//                district.setCityId(cursor.getInt(2));
//                // Thêm vào danh sách.
//                districts.add(district);
//            } while (cursor.moveToNext());
//        }
//
//        // return note list
//        return districts;
//    }

    public void addCity(District city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISTRICT_ID, city.getId());
        values.put(COLUMN_DISTRICT_NAME, city.getName());
        values.put(COLUMN_DISTRICT_CITY_ID, city.getCityId());
        // Insert 1 row to database.
        db.insert(TABLE_DISTRICT, null, values);
        // close connect database
        db.close();
    }

    public void addAllCities(List<District> districts) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (District district : districts) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DISTRICT_ID, district.getId());
            values.put(COLUMN_DISTRICT_NAME, district.getName());
            values.put(COLUMN_DISTRICT_CITY_ID, district.getCityId());
            // Insert 1 row to database.
            db.insert(TABLE_DISTRICT, null, values);
        }
        db.close();
    }


    public void deleteCity(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DISTRICT, COLUMN_DISTRICT_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void insertData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query ="INSERT INTO "+TABLE_DISTRICT+" (`id`, `name`, `city_id`) VALUES\n" +
                "(001,'Quận Ba Đình',01),\n" +
                "(002,'Quận Hoàn Kiếm',01),\n" +
                "(003,'Quận Tây Hồ',01),\n" +
                "(004,'Quận Long Biên',01),\n" +
                "(005,'Quận Cầu Giấy',01),\n" +
                "(006,'Quận Đống Đa',01),\n" +
                "(007,'Quận Hai Bà Trưng',01),\n" +
                "(008,'Quận Hoàng Mai',01),\n" +
                "(009,'Quận Thanh Xuân',01),\n"+
                "(016,'Huyện Sóc Sơn',01),\n" +
                "(017,'Huyện Đông Anh',01),\n" +
                "(018,'Huyện Gia Lâm',01),\n" +
                "(019,'Huyện Từ Liêm',01),\n" +
                "(020,'Huyện Thanh Trì',01),\n" +
                "(024,'Thị Xã Hà Giang',02),\n" +
                "(026,'Huyện Đồng Văn',02),\n" +
                "(027,'Huyện Mèo Vạc',02),\n" +
                "(028,'Huyện Yên Minh',02),\n" +
                "(029,'Huyện Quản Bạ',02),\n" +
                "(030,'Huyện Vị Xuyên',02),\n" +
                "(031,'Huyện Bắc Mê',02),\n" +
                "(032,'Huyện Hoàng Su Phì',02),\n" +
                "(033,'Huyện Xín Mần',02),\n" +
                "(034,'Huyện Bắc Quang',02),\n" +
                "(035,'Huyện Quang Bình',02),\n" +
                "(040,'Thị Xã Cao Bằng',04),\n" +
                "(042,'Huyện Bảo Lâm',04),\n" +
                "(043,'Huyện Bảo Lạc',04),\n" +
                "(044,'Huyện Thông Nông',04),\n" +
                "(045,'Huyện Hà Quảng',04),\n" +
                "(046,'Huyện Trà Lĩnh',04),\n" +
                "(047,'Huyện Trùng Khánh',04),\n" +
                "(048,'Huyện Hạ Lang',04),\n" +
                "(049,'Huyện Quảng Uyên',04),\n" +
                "(050,'Huyện Phục Hoà',04),\n" +
                "(051,'Huyện Hoà An',04),\n" +
                "(052,'Huyện Nguyên Bình',04),\n" +
                "(053,'Huyện Thạch An',04),\n" +
                "(058,'Thị Xã Bắc Kạn',06),\n" +
                "(060,'Huyện Pác Nặm',06),\n" +
                "(061,'Huyện Ba Bể',06),\n" +
                "(062,'Huyện Ngân Sơn',06),\n" +
                "(063,'Huyện Bạch Thông',06),\n" +
                "(064,'Huyện Chợ Đồn',06),\n" +
                "(065,'Huyện Chợ Mới',06),\n" +
                "(066,'Huyện Na Rì',06),\n" +
                "(070,'Thị Xã Tuyên Quang',08),\n" +
                "(072,'Huyện Nà Hang',08),\n" +
                "(073,'Huyện Chiêm Hóa',08),\n" +
                "(074,'Huyện Hàm Yên',08),\n" +
                "(075,'Huyện Yên Sơn',08),\n" +
                "(076,'Huyện Sơn Dương',08),\n" +
                "(080,'Thành Phố Lào Cai',10),\n" +
                "(082,'Huyện Bát Xát',10),\n" +
                "(083,'Huyện Mường Khương',10),\n" +
                "(084,'Huyện Si Ma Cai',10),\n" +
                "(085,'Huyện Bắc Hà',10),\n" +
                "(086,'Huyện Bảo Thắng',10),\n" +
                "(087,'Huyện Bảo Yên',10),\n" +
                "(088,'Huyện Sa Pa',10),\n" +
                "(089,'Huyện Văn Bàn',10),\n" +
                "(094,'Thành Phố Điện Biên Phủ',11),\n" +
                "(095,'Thị Xã Mường Lay',11),\n" +
                "(096,'Huyện Mường Nhé',11),\n" +
                "(097,'Huyện Mường Chà',11),\n" +
                "(098,'Huyện Tủa Chùa',11),\n" +
                "(099,'Huyện Tuần Giáo',11),\n" +
                "(100,'Huyện Điện Biên',11),\n" +
                "(101,'Huyện Điện Biên Đông',11),\n" +
                "(102,'Huyện Mường Ảng',11),\n" +
                "(104,'Thị Xã Lai Châu',12),\n" +
                "(106,'Huyện Tam Đường',12),\n" +
                "(107,'Huyện Mường Tè',12),\n" +
                "(108,'Huyện Sìn Hồ',12),\n" +
                "(109,'Huyện Phong Thổ',12),\n" +
                "(110,'Huyện Than Uyên',12),\n" +
                "(111,'Huyện Tân Uyên',12),\n" +
                "(116,'Thành Phố Sơn La',14),\n" +
                "(118,'Huyện Quỳnh Nhai',14),\n" +
                "(119,'Huyện Thuận Châu',14),\n" +
                "(120,'Huyện Mường La',14),\n" +
                "(121,'Huyện Bắc Yên',14),\n" +
                "(122,'Huyện Phù Yên',14),\n" +
                "(123,'Huyện Mộc Châu',14),\n" +
                "(124,'Huyện Yên Châu',14),\n" +
                "(125,'Huyện Mai Sơn',14),\n" +
                "(126,'Huyện Sông Mã',14),\n" +
                "(127,'Huyện Sốp Cộp',14),\n" +
                "(132,'Thành Phố Yên Bái',15),\n" +
                "(133,'Thị Xã Nghĩa Lộ',15),\n" +
                "(135,'Huyện Lục Yên',15),\n" +
                "(136,'Huyện Văn Yên',15),\n" +
                "(137,'Huyện Mù Cang Chải',15),\n" +
                "(138,'Huyện Trấn Yên',15),\n" +
                "(139,'Huyện Trạm Tấu',15),\n" +
                "(140,'Huyện Văn Chấn',15),\n" +
                "(141,'Huyện Yên Bình',15),\n" +
                "(148,'Thành Phố Hòa Bình',17),\n" +
                "(150,'Huyện Đà Bắc',17),\n" +
                "(151,'Huyện Kỳ Sơn',17),\n" +
                "(152,'Huyện Lương Sơn',17),\n" +
                "(153,'Huyện Kim Bôi',17),\n" +
                "(154,'Huyện Cao Phong',17),\n" +
                "(155,'Huyện Tân Lạc',17),\n" +
                "(156,'Huyện Mai Châu',17),\n" +
                "(157,'Huyện Lạc Sơn',17),\n" +
                "(158,'Huyện Yên Thủy',17),\n" +
                "(159,'Huyện Lạc Thủy',17),\n" +
                "(164,'Thành Phố Thái Nguyên',19),\n" +
                "(165,'Thị Xã Sông Công',19),\n" +
                "(167,'Huyện Định Hóa',19),\n" +
                "(168,'Huyện Phú Lương',19),\n" +
                "(169,'Huyện Đồng Hỷ',19),\n" +
                "(170,'Huyện Võ Nhai',19),\n" +
                "(171,'Huyện Đại Từ',19),\n" +
                "(172,'Huyện Phổ Yên',19),\n" +
                "(173,'Huyện Phú Bình',19),\n" +
                "(178,'Thành Phố Lạng Sơn',20),\n" +
                "(180,'Huyện Tràng Định',20),\n" +
                "(181,'Huyện Bình Gia',20),\n" +
                "(182,'Huyện Văn Lãng',20),\n" +
                "(183,'Huyện Cao Lộc',20),\n" +
                "(184,'Huyện Văn Quan',20),\n" +
                "(185,'Huyện Bắc Sơn',20),\n" +
                "(186,'Huyện Hữu Lũng',20),\n" +
                "(187,'Huyện Chi Lăng',20),\n" +
                "(188,'Huyện Lộc Bình',20),\n" +
                "(189,'Huyện Đình Lập',20),\n" +
                "(193,'Thành Phố Hạ Long',22),\n" +
                "(194,'Thành Phố Móng Cái',22),\n" +
                "(195,'Thị Xã Cẩm Phả',22),\n" +
                "(196,'Thị Xã Uông Bí',22),\n" +
                "(198,'Huyện Bình Liêu',22),\n" +
                "(199,'Huyện Tiên Yên',22),\n" +
                "(200,'Huyện Đầm Hà',22),\n" +
                "(201,'Huyện Hải Hà',22),\n" +
                "(202,'Huyện Ba Chẽ',22),\n" +
                "(203,'Huyện Vân Đồn',22),\n" +
                "(204,'Huyện Hoành Bồ',22),\n" +
                "(205,'Huyện Đông Triều',22),\n" +
                "(206,'Huyện Yên Hưng',22),\n" +
                "(207,'Huyện Cô Tô',22),\n" +
                "(213,'Thành Phố Bắc Giang',24),\n" +
                "(215,'Huyện Yên Thế',24),\n" +
                "(216,'Huyện Tân Yên',24),\n" +
                "(217,'Huyện Lạng Giang',24),\n" +
                "(218,'Huyện Lục Nam',24),\n" +
                "(219,'Huyện Lục Ngạn',24),\n" +
                "(220,'Huyện Sơn Động',24),\n" +
                "(221,'Huyện Yên Dũng',24),\n" +
                "(222,'Huyện Việt Yên',24),\n" +
                "(223,'Huyện Hiệp Hòa',24),\n" +
                "(227,'Thành Phố Việt Trì',25),\n" +
                "(228,'Thị Xã Phú Thọ',25),\n" +
                "(230,'Huyện Đoan Hùng',25),\n" +
                "(231,'Huyện Hạ Hoà',25),\n" +
                "(232,'Huyện Thanh Ba',25),\n" +
                "(233,'Huyện Phù Ninh',25),\n" +
                "(234,'Huyện Yên Lập',25),\n" +
                "(235,'Huyện Cẩm Khê',25),\n" +
                "(236,'Huyện Tam Nông',25),\n" +
                "(237,'Huyện Lâm Thao',25),\n" +
                "(238,'Huyện Thanh Sơn',25),\n" +
                "(239,'Huyện Thanh Thuỷ',25),\n" +
                "(240,'Huyện Tân Sơn',25),\n" +
                "(243,'Thành Phố Vĩnh Yên',26),\n" +
                "(244,'Thị Xã Phúc Yên',26),\n" +
                "(246,'Huyện Lập Thạch',26),\n" +
                "(247,'Huyện Tam Dương',26),\n" +
                "(248,'Huyện Tam Đảo',26),\n" +
                "(249,'Huyện Bình Xuyên',26),\n" +
                "(250,'Huyện Mê Linh',01),\n" +
                "(251,'Huyện Yên Lạc',26),\n" +
                "(252,'Huyện Vĩnh Tường',26),\n" +
                "(253,'Huyện Sông Lô',26),\n" +
                "(256,'Thành Phố Bắc Ninh',27),\n" +
                "(258,'Huyện Yên Phong',27),\n" +
                "(259,'Huyện Quế Võ',27),\n" +
                "(260,'Huyện Tiên Du',27),\n" +
                "(261,'Thị Xã Từ Sơn',27),\n" +
                "(262,'Huyện Thuận Thành',27),\n" +
                "(263,'Huyện Gia Bình',27),\n" +
                "(264,'Huyện Lương Tài',27),\n" +
                "(268,'Quận Hà Đông',01),\n" +
                "(269,'Thị Xã Sơn Tây',01),\n" +
                "(271,'Huyện Ba Vì',01),\n" +
                "(272,'Huyện Phúc Thọ',01),\n" +
                "(273,'Huyện Đan Phượng',01),\n" +
                "(274,'Huyện Hoài Đức',01),\n" +
                "(275,'Huyện Quốc Oai',01),\n" +
                "(276,'Huyện Thạch Thất',01),\n" +
                "(277,'Huyện Chương Mỹ',01),\n" +
                "(278,'Huyện Thanh Oai',01),\n" +
                "(279,'Huyện Thường Tín',01),\n" +
                "(280,'Huyện Phú Xuyên',01),\n" +
                "(281,'Huyện Ứng Hòa',01),\n" +
                "(282,'Huyện Mỹ Đức',01),\n" +
                "(288,'Thành Phố Hải Dương',30),\n" +
                "(290,'Huyện Chí Linh',30),\n" +
                "(291,'Huyện Nam Sách',30),\n" +
                "(292,'Huyện Kinh Môn',30),\n" +
                "(293,'Huyện Kim Thành',30),\n" +
                "(294,'Huyện Thanh Hà',30),\n" +
                "(295,'Huyện Cẩm Giàng',30),\n" +
                "(296,'Huyện Bình Giang',30),\n" +
                "(297,'Huyện Gia Lộc',30),\n" +
                "(298,'Huyện Tứ Kỳ',30),\n" +
                "(299,'Huyện Ninh Giang',30),\n" +
                "(300,'Huyện Thanh Miện',30),\n" +
                "(303,'Quận Hồng Bàng',31),\n" +
                "(304,'Quận Ngô Quyền',31),\n" +
                "(305,'Quận Lê Chân',31),\n" +
                "(306,'Quận Hải An',31),\n" +
                "(307,'Quận Kiến An',31),\n" +
                "(308,'Quận Đồ Sơn',31),\n" +
                "(309,'Quận Kinh Dương',31),\n" +
                "(311,'Huyện Thuỷ Nguyên',31),\n" +
                "(312,'Huyện An Dương',31),\n" +
                "(313,'Huyện An Lão',31),\n" +
                "(314,'Huyện Kiến Thụy',31),\n" +
                "(315,'Huyện Tiên Lãng',31),\n" +
                "(316,'Huyện Vĩnh Bảo',31),\n" +
                "(317,'Huyện Cát Hải',31),\n" +
                "(318,'Huyện Bạch Long Vĩ',31),\n" +
                "(323,'Thành Phố Hưng Yên',33),\n" +
                "(325,'Huyện Văn Lâm',33),\n" +
                "(326,'Huyện Văn Giang',33),\n" +
                "(327,'Huyện Yên Mỹ',33),\n" +
                "(328,'Huyện Mỹ Hào',33),\n" +
                "(329,'Huyện Ân Thi',33),\n" +
                "(330,'Huyện Khoái Châu',33),\n" +
                "(331,'Huyện Kim Động',33),\n" +
                "(332,'Huyện Tiên Lữ',33),\n" +
                "(333,'Huyện Phù Cừ',33),\n" +
                "(336,'Thành Phố Thái Bình',34),\n" +
                "(338,'Huyện Quỳnh Phụ',34),\n" +
                "(339,'Huyện Hưng Hà',34),\n" +
                "(340,'Huyện Đông Hưng',34),\n" +
                "(341,'Huyện Thái Thụy',34),\n" +
                "(342,'Huyện Tiền Hải',34),\n" +
                "(343,'Huyện Kiến Xương',34),\n" +
                "(344,'Huyện Vũ Thư',34),\n" +
                "(347,'Thành Phố Phủ Lý',35),\n" +
                "(349,'Huyện Duy Tiên',35),\n" +
                "(350,'Huyện Kim Bảng',35),\n" +
                "(351,'Huyện Thanh Liêm',35),\n" +
                "(352,'Huyện Bình Lục',35),\n" +
                "(353,'Huyện Lý Nhân',35),\n" +
                "(356,'Thành Phố Nam Định',36),\n" +
                "(358,'Huyện Mỹ Lộc',36),\n" +
                "(359,'Huyện Vụ Bản',36),\n" +
                "(360,'Huyện Ý Yên',36),\n" +
                "(361,'Huyện Nghĩa Hưng',36),\n" +
                "(362,'Huyện Nam Trực',36),\n" +
                "(363,'Huyện Trực Ninh',36),\n" +
                "(364,'Huyện Xuân Trường',36),\n" +
                "(365,'Huyện Giao Thủy',36),\n" +
                "(366,'Huyện Hải Hậu',36),\n" +
                "(369,'Thành Phố Ninh Bình',37),\n" +
                "(370,'Thị Xã Tam Điệp',37),\n" +
                "(372,'Huyện Nho Quan',37),\n" +
                "(373,'Huyện Gia Viễn',37),\n" +
                "(374,'Huyện Hoa Lư',37),\n" +
                "(375,'Huyện Yên Khánh',37),\n" +
                "(376,'Huyện Kim Sơn',37),\n" +
                "(377,'Huyện Yên Mô',37),\n" +
                "(380,'Thành Phố Thanh Hóa',38),\n" +
                "(381,'Thị Xã Bỉm Sơn',38),\n" +
                "(382,'Thị Xã Sầm Sơn',38),\n" +
                "(384,'Huyện Mường Lát',38),\n" +
                "(385,'Huyện Quan Hóa',38),\n" +
                "(386,'Huyện Bá Thước',38),\n" +
                "(387,'Huyện Quan Sơn',38),\n" +
                "(388,'Huyện Lang Chánh',38),\n" +
                "(389,'Huyện Ngọc Lặc',38),\n" +
                "(390,'Huyện Cẩm Thủy',38),\n" +
                "(391,'Huyện Thạch Thành',38),\n" +
                "(392,'Huyện Hà Trung',38),\n" +
                "(393,'Huyện Vĩnh Lộc',38),\n" +
                "(394,'Huyện Yên Định',38),\n" +
                "(395,'Huyện Thọ Xuân',38),\n" +
                "(396,'Huyện Thường Xuân',38),\n" +
                "(397,'Huyện Triệu Sơn',38),\n" +
                "(398,'Huyện Thiệu Hoá',38),\n" +
                "(399,'Huyện Hoằng Hóa',38),\n" +
                "(400,'Huyện Hậu Lộc',38),\n" +
                "(401,'Huyện Nga Sơn',38),\n" +
                "(402,'Huyện Như Xuân',38),\n" +
                "(403,'Huyện Như Thanh',38),\n" +
                "(404,'Huyện Nông Cống',38),\n" +
                "(405,'Huyện Đông Sơn',38),\n" +
                "(406,'Huyện Quảng Xương',38),\n" +
                "(407,'Huyện Tĩnh Gia',38),\n" +
                "(412,'Thành Phố Vinh',40),\n" +
                "(413,'Thị Xã Cửa Lò',40),\n" +
                "(414,'Thị Xã Thái Hoà',40),\n" +
                "(415,'Huyện Quế Phong',40),\n" +
                "(416,'Huyện Quỳ Châu',40),\n" +
                "(417,'Huyện Kỳ Sơn',40),\n" +
                "(418,'Huyện Tương Dương',40),\n" +
                "(419,'Huyện Nghĩa Đàn',40),\n" +
                "(420,'Huyện Quỳ Hợp',40),\n" +
                "(421,'Huyện Quỳnh Lưu',40),\n" +
                "(422,'Huyện Con Cuông',40),\n" +
                "(423,'Huyện Tân Kỳ',40),\n" +
                "(424,'Huyện Anh Sơn',40),\n" +
                "(425,'Huyện Diễn Châu',40),\n" +
                "(426,'Huyện Yên Thành',40),\n" +
                "(427,'Huyện Đô Lương',40),\n" +
                "(428,'Huyện Thanh Chương',40),\n" +
                "(429,'Huyện Nghi Lộc',40),\n" +
                "(430,'Huyện Nam Đàn',40),\n" +
                "(431,'Huyện Hưng Nguyên',40),\n" +
                "(436,'Thành Phố Hà Tĩnh',42),\n" +
                "(437,'Thị Xã Hồng Lĩnh',42),\n" +
                "(439,'Huyện Hương Sơn',42),\n" +
                "(440,'Huyện Đức Thọ',42),\n" +
                "(441,'Huyện Vũ Quang',42),\n" +
                "(442,'Huyện Nghi Xuân',42),\n" +
                "(443,'Huyện Can Lộc',42),\n" +
                "(444,'Huyện Hương Khê',42),\n" +
                "(445,'Huyện Thạch Hà',42),\n" +
                "(446,'Huyện Cẩm Xuyên',42),\n" +
                "(447,'Huyện Kỳ Anh',42),\n" +
                "(448,'Huyện Lộc Hà',42),\n" +
                "(450,'Thành Phố Đồng Hới',44),\n" +
                "(452,'Huyện Minh Hóa',44),\n" +
                "(453,'Huyện Tuyên Hóa',44),\n" +
                "(454,'Huyện Quảng Trạch',44),\n" +
                "(455,'Huyện Bố Trạch',44),\n" +
                "(456,'Huyện Quảng Ninh',44),\n" +
                "(457,'Huyện Lệ Thủy',44),\n" +
                "(461,'Thành Phố Đông Hà',45),\n" +
                "(462,'Thị Xã Quảng Trị',45),\n" +
                "(464,'Huyện Vĩnh Linh',45),\n" +
                "(465,'Huyện Hướng Hóa',45),\n" +
                "(466,'Huyện Gio Linh',45),\n" +
                "(467,'Huyện Đa Krông',45),\n" +
                "(468,'Huyện Cam Lộ',45),\n" +
                "(469,'Huyện Triệu Phong',45),\n" +
                "(470,'Huyện Hải Lăng',45),\n" +
                "(471,'Huyện Cồn Cỏ',45),\n" +
                "(474,'Thành Phố Huế',46),\n" +
                "(476,'Huyện Phong Điền',46),\n" +
                "(477,'Huyện Quảng Điền',46),\n" +
                "(478,'Huyện Phú Vang',46),\n" +
                "(479,'Huyện Hương Thủy',46),\n" +
                "(480,'Huyện Hương Trà',46),\n" +
                "(481,'Huyện A Lưới',46),\n" +
                "(482,'Huyện Phú Lộc',46),\n" +
                "(483,'Huyện Nam Đông',46),\n" +
                "(490,'Quận Liên Chiểu',48),\n" +
                "(491,'Quận Thanh Khê',48),\n" +
                "(492,'Quận Hải Châu',48),\n" +
                "(493,'Quận Sơn Trà',48),\n" +
                "(494,'Quận Ngũ Hành Sơn',48),\n" +
                "(495,'Quận Cẩm Lệ',48),\n" +
                "(497,'Huyện Hoà Vang',48),\n" +
                "(498,'Huyện Hoàng Sa',48),\n" +
                "(502,'Thành Phố Tam Kỳ',49),\n" +
                "(503,'Thành Phố Hội An',49),\n" +
                "(504,'Huyện Tây Giang',49),\n" +
                "(505,'Huyện Đông Giang',49),\n" +
                "(506,'Huyện Đại Lộc',49),\n" +
                "(507,'Huyện Điện Bàn',49),\n" +
                "(508,'Huyện Duy Xuyên',49),\n" +
                "(509,'Huyện Quế Sơn',49),\n" +
                "(510,'Huyện Nam Giang',49),\n" +
                "(511,'Huyện Phước Sơn',49),\n" +
                "(512,'Huyện Hiệp Đức',49),\n" +
                "(513,'Huyện Thăng Bình',49),\n" +
                "(514,'Huyện Tiên Phước',49),\n" +
                "(515,'Huyện Bắc Trà My',49),\n" +
                "(516,'Huyện Nam Trà My',49),\n" +
                "(517,'Huyện Núi Thành',49),\n" +
                "(518,'Huyện Phú Ninh',49),\n" +
                "(519,'Huyện Nông Sơn',49),\n" +
                "(522,'Thành Phố Quảng Ngãi',51),\n" +
                "(524,'Huyện Bình Sơn',51),\n" +
                "(525,'Huyện Trà Bồng',51),\n" +
                "(526,'Huyện Tây Trà',51),\n" +
                "(527,'Huyện Sơn Tịnh',51),\n" +
                "(528,'Huyện Tư Nghĩa',51),\n" +
                "(529,'Huyện Sơn Hà',51),\n" +
                "(530,'Huyện Sơn Tây',51),\n" +
                "(531,'Huyện Minh Long',51),\n" +
                "(532,'Huyện Nghĩa Hành',51),\n" +
                "(533,'Huyện Mộ Đức',51),\n" +
                "(534,'Huyện Đức Phổ',51),\n" +
                "(535,'Huyện Ba Tơ',51),\n" +
                "(536,'Huyện Lý Sơn',51),\n" +
                "(540,'Thành Phố Qui Nhơn',52),\n" +
                "(542,'Huyện An Lão',52),\n" +
                "(543,'Huyện Hoài Nhơn',52),\n" +
                "(544,'Huyện Hoài Ân',52),\n" +
                "(545,'Huyện Phù Mỹ',52),\n" +
                "(546,'Huyện Vĩnh Thạnh',52),\n" +
                "(547,'Huyện Tây Sơn',52),\n" +
                "(548,'Huyện Phù Cát',52),\n" +
                "(549,'Huyện An Nhơn',52),\n" +
                "(550,'Huyện Tuy Phước',52),\n" +
                "(551,'Huyện Vân Canh',52),\n" +
                "(555,'Thành Phố Tuy Hòa',54),\n" +
                "(557,'Thị Xã Sông Cầu',54),\n" +
                "(558,'Huyện Đồng Xuân',54),\n" +
                "(559,'Huyện Tuy An',54),\n" +
                "(560,'Huyện Sơn Hòa',54),\n" +
                "(561,'Huyện Sông Hinh',54),\n" +
                "(562,'Huyện Tây Hoà',54),\n" +
                "(563,'Huyện Phú Hoà',54),\n" +
                "(564,'Huyện Đông Hoà',54),\n" +
                "(568,'Thành Phố Nha Trang',56),\n" +
                "(569,'Thị Xã Cam Ranh',56),\n" +
                "(570,'Huyện Cam Lâm',56),\n" +
                "(571,'Huyện Vạn Ninh',56),\n" +
                "(572,'Huyện Ninh Hòa',56),\n" +
                "(573,'Huyện Khánh Vĩnh',56),\n" +
                "(574,'Huyện Diên Khánh',56),\n" +
                "(575,'Huyện Khánh Sơn',56),\n" +
                "(576,'Huyện Trường Sa',56),\n" +
                "(582,'Thành Phố Phan Rang-Tháp Chàm',58),\n" +
                "(584,'Huyện Bác Ái',58),\n" +
                "(585,'Huyện Ninh Sơn',58),\n" +
                "(586,'Huyện Ninh Hải',58),\n" +
                "(587,'Huyện Ninh Phước',58),\n" +
                "(588,'Huyện Thuận Bắc',58),\n" +
                "(589,'Huyện Thuận Nam',58),\n" +
                "(593,'Thành Phố Phan Thiết',60),\n" +
                "(594,'Thị Xã La Gi',60),\n" +
                "(595,'Huyện Tuy Phong',60),\n" +
                "(596,'Huyện Bắc Bình',60),\n" +
                "(597,'Huyện Hàm Thuận Bắc',60),\n" +
                "(598,'Huyện Hàm Thuận Nam',60),\n" +
                "(599,'Huyện Tánh Linh',60),\n" +
                "(600,'Huyện Đức Linh',60),\n" +
                "(601,'Huyện Hàm Tân',60),\n" +
                "(602,'Huyện Phú Quí',60),\n" +
                "(608,'Thành Phố Kon Tum',62),\n" +
                "(610,'Huyện Đắk Glei',62),\n" +
                "(611,'Huyện Ngọc Hồi',62),\n" +
                "(612,'Huyện Đắk Tô',62),\n" +
                "(613,'Huyện Kon Plông',62),\n" +
                "(614,'Huyện Kon Rẫy',62),\n" +
                "(615,'Huyện Đắk Hà',62),\n" +
                "(616,'Huyện Sa Thầy',62),\n" +
                "(617,'Huyện Tu Mơ Rông',62),\n" +
                "(622,'Thành Phố Pleiku',64),\n" +
                "(623,'Thị Xã An Khê',64),\n" +
                "(624,'Thị Xã Ayun Pa',64),\n" +
                "(625,'Huyện Kbang',64),\n" +
                "(626,'Huyện Đăk Đoa',64),\n" +
                "(627,'Huyện Chư Păh',64),\n" +
                "(628,'Huyện Ia Grai',64),\n" +
                "(629,'Huyện Mang Yang',64),\n" +
                "(630,'Huyện Kông Chro',64),\n" +
                "(631,'Huyện Đức Cơ',64),\n" +
                "(632,'Huyện Chư Prông',64),\n" +
                "(633,'Huyện Chư Sê',64),\n" +
                "(634,'Huyện Đăk Pơ',64),\n" +
                "(635,'Huyện Ia Pa',64),\n" +
                "(637,'Huyện Krông Pa',64),\n" +
                "(638,'Huyện Phú Thiện',64),\n" +
                "(639,'Huyện Chư Pưh',64),\n" +
                "(643,'Thành Phố Buôn Ma Thuột',66),\n" +
                "(644,'Thị Xã Buôn Hồ',66),\n" +
                "(645,'Huyện Ea Hleo',66),\n" +
                "(646,'Huyện Ea Súp',66),\n" +
                "(647,'Huyện Buôn Đôn',66),\n" +
                "(648,'Huyện Cư Mgar',66),\n" +
                "(649,'Huyện Krông Búk',66),\n" +
                "(650,'Huyện Krông Năng',66),\n" +
                "(651,'Huyện Ea Kar',66),\n" +
                "(652,'Huyện Mđrắk',66),\n" +
                "(653,'Huyện Krông Bông',66),\n" +
                "(654,'Huyện Krông Pắc',66),\n" +
                "(655,'Huyện Krông A Na',66),\n" +
                "(656,'Huyện Lắk',66),\n" +
                "(657,'Huyện Cư Kuin',66),\n" +
                "(660,'Thị Xã Gia Nghĩa',67),\n" +
                "(661,'Huyện Đắk Glong',67),\n" +
                "(662,'Huyện Cư Jút',67),\n" +
                "(663,'Huyện Đắk Mil',67),\n" +
                "(664,'Huyện Krông Nô',67),\n" +
                "(665,'Huyện Đắk Song',67),\n" +
                "(666,'Huyện Đắk Rlấp',67),\n" +
                "(667,'Huyện Tuy Đức',67),\n" +
                "(672,'Thành Phố Đà Lạt',68),\n" +
                "(673,'Thị Xã Bảo Lộc',68),\n" +
                "(674,'Huyện Đam Rông',68),\n" +
                "(675,'Huyện Lạc Dương',68),\n" +
                "(676,'Huyện Lâm Hà',68),\n" +
                "(677,'Huyện Đơn Dương',68),\n" +
                "(678,'Huyện Đức Trọng',68),\n" +
                "(679,'Huyện Di Linh',68),\n" +
                "(680,'Huyện Bảo Lâm',68),\n" +
                "(681,'Huyện Đạ Huoai',68),\n" +
                "(682,'Huyện Đạ Tẻh',68),\n" +
                "(683,'Huyện Cát Tiên',68),\n" +
                "(688,'Thị Xã Phước Long',70),\n" +
                "(689,'Thị Xã Đồng Xoài',70),\n" +
                "(690,'Thị Xã Bình Long',70),\n" +
                "(691,'Huyện Bù Gia Mập',70),\n" +
                "(692,'Huyện Lộc Ninh',70),\n" +
                "(693,'Huyện Bù Đốp',70),\n" +
                "(694,'Huyện Hớn Quản',70),\n" +
                "(695,'Huyện Đồng Phù',70),\n" +
                "(696,'Huyện Bù Đăng',70),\n" +
                "(697,'Huyện Chơn Thành',70),\n" +
                "(703,'Thị Xã Tây Ninh',72),\n" +
                "(705,'Huyện Tân Biên',72),\n" +
                "(706,'Huyện Tân Châu',72),\n" +
                "(707,'Huyện Dương Minh Châu',72),\n" +
                "(708,'Huyện Châu Thành',72),\n" +
                "(709,'Huyện Hòa Thành',72),\n" +
                "(710,'Huyện Gò Dầu',72),\n" +
                "(711,'Huyện Bến Cầu',72),\n" +
                "(712,'Huyện Trảng Bàng',72),\n" +
                "(718,'Thị Xã Thủ Dầu Một',74),\n" +
                "(720,'Huyện Dầu Tiếng',74),\n" +
                "(721,'Huyện Bến Cát',74),\n" +
                "(722,'Huyện Phú Giáo',74),\n" +
                "(723,'Huyện Tân Uyên',74),\n" +
                "(724,'Huyện Dĩ An',74),\n" +
                "(725,'Huyện Thuận An',74),\n" +
                "(731,'Thành Phố Biên Hòa',75),\n" +
                "(732,'Thị Xã Long Khánh',75),\n" +
                "(734,'Huyện Tân Phú',75),\n" +
                "(735,'Huyện Vĩnh Cửu',75),\n" +
                "(736,'Huyện Định Quán',75),\n" +
                "(737,'Huyện Trảng Bom',75),\n" +
                "(738,'Huyện Thống Nhất',75),\n" +
                "(739,'Huyện Cẩm Mỹ',75),\n" +
                "(740,'Huyện Long Thành',75),\n" +
                "(741,'Huyện Xuân Lộc',75),\n" +
                "(742,'Huyện Nhơn Trạch',75),\n" +
                "(747,'Thành Phố Vũng Tầu',77),\n" +
                "(748,'Thị Xã Bà Rịa',77),\n" +
                "(750,'Huyện Châu Đức',77),\n" +
                "(751,'Huyện Xuyên Mộc',77),\n" +
                "(752,'Huyện Long Điền',77),\n" +
                "(753,'Huyện Đất Đỏ',77),\n" +
                "(754,'Huyện Tân Thành',77),\n" +
                "(755,'Huyện Côn Đảo',77),\n" +
                "(760,'Quận 1',79),\n" +
                "(761,'Quận 12',79),\n" +
                "(762,'Quận Thủ Đức',79),\n" +
                "(763,'Quận 9',79),\n" +
                "(764,'Quận Gò Vấp',79),\n" +
                "(765,'Quận Bình Thạnh',79),\n" +
                "(766,'Quận Tân Bình',79),\n" +
                "(767,'Quận Tân Phú',79),\n" +
                "(768,'Quận Phú Nhuận',79),\n" +
                "(769,'Quận 2',79),\n" +
                "(770,'Quận 3',79),\n" +
                "(771,'Quận 10',79),\n" +
                "(772,'Quận 11',79),\n" +
                "(773,'Quận 4',79),\n" +
                "(774,'Quận 5',79),\n" +
                "(775,'Quận 6',79),\n" +
                "(776,'Quận 8',79),\n" +
                "(777,'Quận Bình Tân',79),\n" +
                "(778,'Quận 7',79),\n" +
                "(783,'Huyện Củ Chi',79),\n" +
                "(784,'Huyện Hóc Môn',79),\n" +
                "(785,'Huyện Bình Chánh',79),\n" +
                "(786,'Huyện Nhà Bè',79),\n" +
                "(787,'Huyện Cần Giờ',79),\n" +
                "(794,'Thành Phố Tân An',80),\n" +
                "(796,'Huyện Tân Hưng',80),\n" +
                "(797,'Huyện Vĩnh Hưng',80),\n" +
                "(798,'Huyện Mộc Hóa',80),\n" +
                "(799,'Huyện Tân Thạnh',80),\n" +
                "(800,'Huyện Thạnh Hóa',80),\n" +
                "(801,'Huyện Đức Huệ',80),\n" +
                "(802,'Huyện Đức Hòa',80),\n" +
                "(803,'Huyện Bến Lức',80),\n" +
                "(804,'Huyện Thủ Thừa',80),\n" +
                "(805,'Huyện Tân Trụ',80),\n" +
                "(806,'Huyện Cần Đước',80),\n" +
                "(807,'Huyện Cần Giuộc',80),\n" +
                "(808,'Huyện Châu Thành',80),\n" +
                "(815,'Thành Phố Mỹ Tho',82),\n" +
                "(816,'Thị Xã Gò Công',82),\n" +
                "(818,'Huyện Tân Phước',82),\n" +
                "(819,'Huyện Cái Bè',82),\n" +
                "(820,'Huyện Cai Lậy',82),\n" +
                "(821,'Huyện Châu Thành',82),\n" +
                "(822,'Huyện Chợ Gạo',82),\n" +
                "(823,'Huyện Gò Công Tây',82),\n" +
                "(824,'Huyện Gò Công Đông',82),\n" +
                "(825,'Huyện Tân Phú Đông',82),\n" +
                "(829,'Thành Phố Bến Tre',83),\n" +
                "(831,'Huyện Châu Thành',83),\n" +
                "(832,'Huyện Chợ Lách',83),\n" +
                "(833,'Huyện Mỏ Cày Nam',83),\n" +
                "(834,'Huyện Giồng Trôm',83),\n" +
                "(835,'Huyện Bình Đại',83),\n" +
                "(836,'Huyện Ba Tri',83),\n" +
                "(837,'Huyện Thạnh Phú',83),\n" +
                "(838,'Huyện Mỏ Cày Bắc',83),\n" +
                "(842,'Thị Xã Trà Vinh',84),\n" +
                "(844,'Huyện Càng Long',84),\n" +
                "(845,'Huyện Cầu Kè',84),\n" +
                "(846,'Huyện Tiểu Cần',84),\n" +
                "(847,'Huyện Châu Thành',84),\n" +
                "(848,'Huyện Cầu Ngang',84),\n" +
                "(849,'Huyện Trà Cú',84),\n" +
                "(850,'Huyện Duyên Hải',84),\n" +
                "(855,'Thành Phố Vĩnh Long',86),\n" +
                "(857,'Huyện Long Hồ',86),\n" +
                "(858,'Huyện Mang Thít',86),\n" +
                "(859,'Huyện Vũng Liêm',86),\n" +
                "(860,'Huyện Tam Bình',86),\n" +
                "(861,'Huyện Bình Minh',86),\n" +
                "(862,'Huyện Trà Ôn',86),\n" +
                "(863,'Huyện Bình Tân',86),\n" +
                "(866,'Thành Phố Cao Lãnh',87),\n" +
                "(867,'Thị Xã Sa Đéc',87),\n" +
                "(868,'Thị Xã Hồng Ngự',87),\n" +
                "(869,'Huyện Tân Hồng',87),\n" +
                "(870,'Huyện Hồng Ngự',87),\n" +
                "(871,'Huyện Tam Nông',87),\n" +
                "(872,'Huyện Tháp Mười',87),\n" +
                "(873,'Huyện Cao Lãnh',87),\n" +
                "(874,'Huyện Thanh Bình',87),\n" +
                "(875,'Huyện Lấp Vò',87),\n" +
                "(876,'Huyện Lai Vung',87),\n" +
                "(877,'Huyện Châu Thành',87),\n" +
                "(883,'Thành Phố Long Xuyên',89),\n" +
                "(884,'Thị Xã Châu Đốc',89),\n" +
                "(886,'Huyện An Phú',89),\n" +
                "(887,'Thị Xã Tân Châu',89),\n" +
                "(888,'Huyện Phú Tân',89),\n" +
                "(889,'Huyện Châu Phú',89),\n" +
                "(890,'Huyện Tịnh Biên',89),\n" +
                "(891,'Huyện Tri Tôn',89),\n" +
                "(892,'Huyện Châu Thành',89),\n" +
                "(893,'Huyện Chợ Mới',89),\n" +
                "(894,'Huyện Thoại Sơn',89),\n" +
                "(899,'Thành Phố Rạch Giá',91),\n" +
                "(900,'Thị Xã Hà Tiên',91),\n" +
                "(902,'Huyện Kiên Lương',91),\n" +
                "(903,'Huyện Hòn Đất',91),\n" +
                "(904,'Huyện Tân Hiệp',91),\n" +
                "(905,'Huyện Châu Thành',91),\n" +
                "(906,'Huyện Giồng Giềng',91),\n" +
                "(907,'Huyện Gò Quao',91),\n" +
                "(908,'Huyện An Biên',91),\n" +
                "(909,'Huyện An Minh',91),\n" +
                "(910,'Huyện Vĩnh Thuận',91),\n" +
                "(911,'Huyện Phú Quốc',91),\n" +
                "(912,'Huyện Kiên Hải',91),\n" +
                "(913,'Huyện U Minh Thượng',91),\n" +
                "(914,'Huyện Giang Thành',91),\n" +
                "(916,'Quận Ninh Kiều',92),\n" +
                "(917,'Quận Ô Môn',92),\n" +
                "(918,'Quận Bình Thuỷ',92),\n" +
                "(919,'Quận Cái Răng',92),\n" +
                "(923,'Quận Thốt Nốt',92),\n" +
                "(924,'Huyện Vĩnh Thạnh',92),\n" +
                "(925,'Huyện Cờ Đỏ',92),\n" +
                "(926,'Huyện Phong Điền',92),\n" +
                "(927,'Huyện Thới Lai',92),\n" +
                "(930,'Thị Xã Vị Thanh',93),\n" +
                "(931,'Thị Xã Ngã Bảy',93),\n" +
                "(932,'Huyện Châu Thành A',93),\n" +
                "(933,'Huyện Châu Thành',93),\n" +
                "(934,'Huyện Phụng Hiệp',93),\n" +
                "(935,'Huyện Vị Thuỷ',93),\n" +
                "(936,'Huyện Long Mỹ',93),\n" +
                "(941,'Thành Phố Sóc Trăng',94),\n" +
                "(942,'Huyện Châu Thành',94),\n" +
                "(943,'Huyện Kế Sách',94),\n" +
                "(944,'Huyện Mỹ Tú',94),\n" +
                "(945,'Huyện Cù Lao Dung',94),\n" +
                "(946,'Huyện Long Phú',94),\n" +
                "(947,'Huyện Mỹ Xuyên',94),\n" +
                "(948,'Huyện Ngã Năm',94),\n" +
                "(949,'Huyện Thạnh Trị',94),\n" +
                "(950,'Huyện Vĩnh Châu',94),\n" +
                "(951,'Huyện Trần Đề',94),\n" +
                "(954,'Thị Xã Bạc Liêu',95),\n" +
                "(956,'Huyện Hồng Dân',95),\n" +
                "(957,'Huyện Phước Long',95),\n" +
                "(958,'Huyện Vĩnh Lợi',95),\n" +
                "(959,'Huyện Giá Rai',95),\n" +
                "(960,'Huyện Đông Hải',95),\n" +
                "(961,'Huyện Hoà Bình',95),\n" +
                "(964,'Thành Phố Cà Mau',96),\n" +
                "(966,'Huyện U Minh',96),\n" +
                "(967,'Huyện Thới Bình',96),\n" +
                "(968,'Huyện Trần Văn Thời',96),\n" +
                "(969,'Huyện Cái Nước',96),\n" +
                "(970,'Huyện Đầm Dơi',96),\n" +
                "(971,'Huyện Năm Căn',96),\n" +
                "(972,'Huyện Phú Tân',96),\n" +
                "(973,'Huyện Ngọc Hiển',96)\n";
        db.execSQL(query);
        db.close();
    }

}
