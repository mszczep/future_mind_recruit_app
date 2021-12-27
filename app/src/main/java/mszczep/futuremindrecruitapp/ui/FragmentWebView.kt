package mszczep.futuremindrecruitapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import mszczep.futuremindrecruitapp.databinding.FragmentWebViewBinding

class FragmentWebView : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val args: FragmentWebViewArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val link = args.descriptionLink
        val webView = binding.webView
        webView.webViewClient = WebViewClient()
        webView.loadUrl(link)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}