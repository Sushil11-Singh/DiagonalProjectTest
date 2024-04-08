import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.diagnal.diagnalprject.model.ContentItems
import com.diagnal.diagnalprject.model.ContentListDTO
import com.diagnal.diagnalprject.model.Page
import com.diagnal.diagnalprject.repository.ContentListRepo
import com.diagnal.diagnalprject.viewmodel.ContentListViewModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.IOException

class ContentListViewModalTest {

    // This rule is used to make LiveData work synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Mock repository
    private lateinit var repository: ContentListRepo

    @Before
    fun setup() {
        repository = mock(ContentListRepo::class.java)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `load_Data`() = runBlockingTest {
        // Mock data to be returned by repository
        val testData = ContentListDTO(Page(ContentItems(emptyList()), "1", "20", "s", "54"))
        `when`(repository.loadDataFromJson(1)).thenReturn(testData)

        // Create ViewModel instance
        val viewModel = ContentListViewModal()
        viewModel.init(repository)

        // Observer for LiveData
        val observer = Observer<ContentListDTO> {}

        try {
            // Observe LiveData and verify that it is updated correctly
            viewModel.myData.observeForever(observer)
            viewModel.loadData(1)

            // Assert that LiveData has been updated correctly
            assertEquals(testData, viewModel.myData.value)
        } finally {
            // Clean up
            viewModel.myData.removeObserver(observer)
            Dispatchers.resetMain()
        }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `loadData_error`() {
        val exception = IOException("Some thing went wrong")
        `when`(repository.loadDataFromJson(1)).thenAnswer {
            throw exception
        }

        // Create ViewModel instance
        val viewModel = ContentListViewModal()
        viewModel.init(repository)

        // Observer for LiveData
        val observer = Observer<ContentListDTO?> {}

        try {
            // Observe LiveData and verify that it is not updated with the error response
            viewModel.myData.observeForever(observer)
            viewModel.loadData(1)

            // Assert that LiveData is null because of the error
            assertEquals(null, viewModel.myData.value)
        } finally {
            // Clean up
            viewModel.myData.removeObserver(observer)

            // Reset the main dispatcher
            Dispatchers.resetMain()
        }
    }

}
