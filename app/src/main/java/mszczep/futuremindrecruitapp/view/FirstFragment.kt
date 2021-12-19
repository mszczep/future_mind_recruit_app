package mszczep.futuremindrecruitapp.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mszczep.futuremindrecruitapp.R
import mszczep.futuremindrecruitapp.databinding.FragmentFirstBinding
import mszczep.futuremindrecruitapp.utils.OnItemClickListener
import mszczep.futuremindrecruitapp.utils.addOnItemClickListener
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

        setHasOptionsMenu(true)
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
                binding.swipeRefreshLayout.isRefreshing = false
                val recyclerView = binding.recyclerView
                recyclerView.visibility = View.VISIBLE
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                recyclerView.adapter = RecyclerViewAdapter(it)
                recyclerView.addOnItemClickListener(object: OnItemClickListener{
                    override fun onItemClicked(position: Int, view: View) {
                        if(it[position].descriptionLink == null)
                            return

                        val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(it[position].descriptionLink!!)
                        findNavController().navigate(action)
                    }

                })
            }
        }
        _viewModel.getWebRecruitmentTaskData.observe(viewLifecycleOwner){
            if(it){
                getDBData()
            }
        }

        _viewModel.errorHandler.observe(viewLifecycleOwner){
            binding.swipeRefreshLayout.isRefreshing = false
            Toast.makeText(requireContext(), "Error handling TODO()", Toast.LENGTH_SHORT).show()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        getDBData()
        return binding.root
    }

    /**
     * Local DB query for data
     */
    private fun getDBData(){
        _viewModel.queryDBGetRecruitmentTaskData()
    }

    private fun loadDataFromNet(){
        binding.recyclerView.visibility = View.GONE
        _viewModel.getWebRecruitmentTaskData()
    }

    private fun refreshData(){
        binding.recyclerView.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
        _viewModel.refreshData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            getDBData()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
//        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId){
            R.id.menu_refresh -> {
                refreshData()
                true
            }
           else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}