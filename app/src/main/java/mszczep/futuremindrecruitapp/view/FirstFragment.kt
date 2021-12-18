package mszczep.futuremindrecruitapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mszczep.futuremindrecruitapp.databinding.FragmentFirstBinding
import mszczep.futuremindrecruitapp.viewModel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val _viewModel: MainActivityViewModel by viewModel()

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        _viewModel.progressBar.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar.visibility = View.VISIBLE
            }
            else{

                binding.progressBar.visibility = View.GONE
            }
        }


        _viewModel.recruitmentData.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                loadDataFromNet()
            }else{
                val recyclerView = binding.recyclerView
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                recyclerView.adapter = RecyclerViewAdapter(it)
            }
        }
        _viewModel.getWebRecruitmentTaskData.observe(viewLifecycleOwner){
            if(it){
                getDBData()
            }
        }

        _viewModel.errorHandler.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "Error handling TODO()", Toast.LENGTH_SHORT).show()
        }



        return binding.root
    }

    /**
     * Local DB query for data
     */
    private fun getDBData(){
        _viewModel.queryDBGetRecruitmentTaskData()
    }

    private fun loadDataFromNet(){
        _viewModel.getWebRecruitmentTaskData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
//            _viewModel.getWebRecruitmentTaskData()
            getDBData()
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}