package magentoegypt.locafy.addons.advance_Report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import magentoegypt.locafy.databinding.FragmentApiFaliureScreenBinding

class ApiFailureScreen : BottomSheetDialogFragment() {


    lateinit var binding: FragmentApiFaliureScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiFaliureScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments == null)
            return
        val msg = requireArguments().getString("data")

        binding.textView5.text = msg
        binding.button.setOnClickListener {
            //Navigation.findNavController(view).navigateUp()
            dismiss()
        }
    }
}