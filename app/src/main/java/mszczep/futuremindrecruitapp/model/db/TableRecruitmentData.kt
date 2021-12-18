package mszczep.futuremindrecruitapp.model.db

import androidx.room.*


@Entity(
    tableName = "recruitment_data"
)

data class TableRecruitmentData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "image_url") var imageUrl: String,
    @ColumnInfo(name = "modification_date") var modificationDate: String,
    @ColumnInfo(name = "order_id") var orderId: Int,
    @ColumnInfo(name = "title") var title: String
)


@Dao
interface ITableRecruitmentData {
    @Insert
    fun insertNewData(data: TableRecruitmentData)

    @Query(""" select * from recruitment_data order by order_id DESC""")
    fun getAll(): List<TableRecruitmentData>

    @Query("""delete from recruitment_data""")
    fun deleteAllData()
}