package com.raithumitra.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.raithumitra.data.local.dao.ContractDao;
import com.raithumitra.data.local.dao.EquipmentDao;
import com.raithumitra.data.local.dao.LaborerDao;
import com.raithumitra.data.local.entity.Contract;
import com.raithumitra.data.local.entity.Equipment;
import com.raithumitra.data.local.entity.Laborer;

@Database(entities = { Contract.class, Equipment.class, Laborer.class }, version = 8, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContractDao contractDao();

    public abstract EquipmentDao equipmentDao();

    public abstract LaborerDao laborerDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "raithu_mitra_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
