package com.raithumitra.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.raithumitra.data.local.entity.Laborer;
import java.util.List;

@Dao
public interface LaborerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Laborer laborer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Laborer> laborers);

    @Query("SELECT * FROM laborers")
    LiveData<List<Laborer>> getAllLaborers();

    @Query("DELETE FROM laborers")
    void deleteAll();
}
