package com.raithumitra.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.raithumitra.data.local.entity.Contract;
import java.util.List;

@Dao
public interface ContractDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contract contract);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Contract> contracts);

    @Query("SELECT * FROM contracts")
    LiveData<List<Contract>> getAllContracts();

    @Query("DELETE FROM contracts")
    void deleteAll();
}
