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
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description_link") var descriptionLink: String?
)


@Dao
interface ITableRecruitmentData {

    /**
     * Database query; inserts data
     * @param data Data to insert
     */
    @Insert
    suspend fun insertNewData(data: TableRecruitmentData)

    /**
     * Database query; returns all data in the table
     * @return A List of all entries
     */
    @Query("""select * from recruitment_data order by order_id DESC""")
    fun getAll(): List<TableRecruitmentData>

    /**
     * Database query; clears the table of all data
     */
    @Query("""delete from recruitment_data""")
    suspend fun deleteAllData(): Int

    /**
     * Database query; returns specific order details
     * @param orderId Order id
     * @return Specific order details
     */
    @Query("""select * from recruitment_data where order_id = :orderId""")
    suspend fun getOrder(orderId: Int): TableRecruitmentData
}