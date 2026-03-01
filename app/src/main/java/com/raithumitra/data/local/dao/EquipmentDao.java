package com.raithumitra.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.raithumitra.data.local.entity.Equipment;
import java.util.List;

@Dao
public interface EquipmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Equipment equipment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Equipment> equipmentList);

    @Query("SELECT * FROM equipment")
    LiveData<List<Equipment>> getAllEquipment();

    @Query("DELETE FROM equipment")
    void deleteAll();
}
