package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserMenuFragment : Fragment() {
    private val userViewModel : UserViewModel by activityViewModels()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = userViewModel.userName.value
        view.findViewById<TextView>(R.id.userMenuWelcomeTextView).text = "Welcome $username"

        view.findViewById<Button>(R.id.userMenuViewBalanceButton).setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_viewBalanceFragment)
        }

        view.findViewById<Button>(R.id.userMenuTransferFundsButton).setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_transferFundsFragment)
        }

        view.findViewById<Button>(R.id.userMenuExchangeCalculatorButton).setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_calculateExchangeFragment)
        }

        view.findViewById<Button>(R.id.userMenuPayBillsButton).setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_payBillsFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserMenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}