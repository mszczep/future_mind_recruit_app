package mszczep.futuremindrecruitapp.ui

import android.os.Bundle
import android.view.*
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mszczep.futuremindrecruitapp.R
import mszczep.futuremindrecruitapp.adapters.RecyclerViewAdapter
import mszczep.futuremindrecruitapp.data.RecruitmentDataState
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
    ): View {

        setHasOptionsMenu(true)
        isTablet = context?.resources?.getBoolean(R.bool.isTablet) ?: false
        _binding = FragmentRecruitmentDataListBinding.inflate(inflater, container, false)

        _viewModel.recruitmentDataState.observe(viewLifecycleOwner){
            when(it){
                is RecruitmentDataState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.constraintLoadingLayout.visibility = View.VISIBLE
                    binding.constraintSuccessLayout.visibility = View.GONE
                    binding.constraintErrorLayout.visibility = View.GONE
                }
                is RecruitmentDataState.Success -> {
                    binding.constraintLoadingLayout.visibility = View.GONE
                    binding.constraintSuccessLayout.visibility = View.VISIBLE
                    binding.constraintErrorLayout.visibility = View.GONE

                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    binding.recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    binding.recyclerView.adapter = RecyclerViewAdapter(it.data)
                    binding.recyclerView.addOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClicked(position: Int, view: View) {
                            if (it.data[position].descriptionLink == null)
                                return

                            if (isTablet) {
                                val webView = binding.webView
                                webView?.webViewClient = WebViewClient()
                                webView?.loadUrl(it.data[position].descriptionLink!!)
                                binding.webView?.visibility = View.VISIBLE
                            } else {
                                val action =
                                    FragmentRecruitmentDataListDirections.actionRecruitmentDataListToWebView(
                                        it.data[position].descriptionLink!!
                                    )
                                findNavController().navigate(action)
                            }
                        }

                    })
                }
                is RecruitmentDataState.Error -> {
                    binding.constraintLoadingLayout.visibility = View.GONE
                    binding.constraintSuccessLayout.visibility = View.GONE
                    binding.constraintErrorLayout.visibility = View.VISIBLE
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        return binding.root
    }

    private fun getData() {
        _viewModel.getData()
    }


    private fun refreshData() {
        _viewModel.refreshData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRetry.setOnClickListener {
            refreshData()
        }

        getData()
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