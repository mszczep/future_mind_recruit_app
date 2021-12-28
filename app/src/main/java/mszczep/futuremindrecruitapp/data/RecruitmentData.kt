package mszczep.futuremindrecruitapp.data

import androidx.room.*

@Entity(
    tableName = "recruitment_data"
)

data class RecruitmentData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "image_url") var imageUrl: String,
    @ColumnInfo(name = "modification_date") var modificationDate: String,
    @ColumnInfo(name = "order_id") var orderId: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description_link") var descriptionLink: String?
)

@Dao
interface RecruitmentDataDao {

    @Insert
    suspend fun insertAll(recruitmentData: List<RecruitmentData>)

    @Query("select * from recruitment_data order by order_id DESC")
    suspend fun getAll(): List<RecruitmentData>

    @Query("delete from recruitment_data")
    suspend fun deleteAllRecruitmentData(): Int

    @Query("select * from recruitment_data where order_id = :orderId")
    suspend fun getRecruitmentDetails(orderId: Int): RecruitmentData
}