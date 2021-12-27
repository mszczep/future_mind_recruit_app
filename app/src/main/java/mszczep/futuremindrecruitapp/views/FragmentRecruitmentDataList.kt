package mszczep.futuremindrecruitapp.views

import android.os.Bundle
import android.view.*
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mszczep.futuremindrecruitapp.R
import mszczep.futuremindrecruitapp.adapters.RecyclerViewAdapter
import mszczep.futuremindrecruitapp.databinding.FragmentRecruitmentDataListBinding
import mszczep.futuremindrecruitapp.utils.OnItemClickListener
import mszczep.futuremindrecruitapp.utils.addOnItemClickListener
import mszczep.futuremindrecruitapp.viewmodels.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentRecruitmentDataList : Fragment() {

    private val _viewModel: MainActivityViewModel by viewModel()

    private var _binding: FragmentRecruitmentDataListBinding? = null

    private val binding get() = _binding!!

    private var isTablet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        isTablet = context?.resources?.getBoolean(R.bool.isTablet) ?: false
        _binding = FragmentRecruitmentDataListBinding.inflate(inflater, container, false)

        _viewModel.progressBar.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar?.visibility = View.VISIBLE
            } else {

                binding.progressBar?.visibility = View.GONE
            }
        }


        _viewModel.recruitmentData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                loadDataFromNet()
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
                val recyclerView = binding.recyclerView
                recyclerView?.visibility = View.VISIBLE

                if (isTablet)
                    binding.theGreatSeparator?.visibility = View.VISIBLE

                recyclerView?.layoutManager = LinearLayoutManager(context)
                recyclerView?.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                recyclerView?.adapter = RecyclerViewAdapter(it)
                recyclerView?.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(position: Int, view: View) {
                        if (it[position].descriptionLink == null)
                            return

                        if (isTablet) {
                            val webView = binding.webView
                            webView?.webViewClient = WebViewClient()
                            webView?.loadUrl(it[position].descriptionLink!!)
                            binding.webView?.visibility = View.VISIBLE
                        } else {
                            val action =
                                FragmentRecruitmentDataListDirections.actionRecruitmentDataListToWebView(
                                    it[position].descriptionLink!!
                                )
                            findNavController().navigate(action)
                        }
                    }

                })
            }
        }
        _viewModel.getWebRecruitmentTaskData.observe(viewLifecycleOwner) {
            if (it) {
                getDBData()
            }
        }

        _viewModel.errorHandler.observe(viewLifecycleOwner) {
            binding.errorText.text = it.second ?: ""
            if (it.first) {
                binding.buttonRetry.visibility = View.VISIBLE
                binding.errorText.visibility = View.VISIBLE
            } else {
                binding.buttonRetry.visibility = View.GONE
                binding.errorText.visibility = View.GONE
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        getDBData()
        return binding.root
    }

    private fun getDBData() {
        _viewModel.queryDBGetRecruitmentTaskData()
    }
    private fun loadDataFromNet() {
        binding.recyclerView?.visibility = View.GONE
        _viewModel.getWebRecruitmentTaskData()
    }

    private fun refreshData() {
        if (isTablet) {
            binding.webView?.visibility = View.GONE
            binding.theGreatSeparator?.visibility = View.GONE
        }

        binding.recyclerView?.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
        _viewModel.refreshData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRetry.setOnClickListener {
            if (isTablet)
                binding.webView?.visibility = View.GONE

            refreshData()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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