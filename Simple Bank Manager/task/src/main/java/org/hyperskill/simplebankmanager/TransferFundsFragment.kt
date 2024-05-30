package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransferFundsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferFundsFragment : Fragment() {
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

        val callback = requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.action_transferFundsFragment_to_userMenuFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer_funds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val balance = userViewModel.balance.value ?: 100.0

        view.findViewById<Button>(R.id.transferFundsButton)
            .setOnClickListener {
                val editAccount = view.findViewById<EditText>(R.id.transferFundsAccountEditText)
                val inputAccountNumber = editAccount.text.toString()
                if (!inputAccountNumber.matches(Regex("([cs])a\\d{4}"))) {
                    editAccount.error = "Invalid account number"
                } else {
                    val editAmount = view.findViewById<EditText>(R.id.transferFundsAmountEditText)
                    var inputAmount = 0.0
                    if (editAmount.text.isNotEmpty()) {
                        inputAmount = editAmount.text.toString().toDouble()
                    }
                    if (inputAmount <= 0 || editAmount.text.isEmpty()) {
                        editAmount.error = "Invalid amount"
                    } else {
                        if (inputAmount > balance) {
                            val toastMsg =
                                String.format("Not enough funds to transfer $%.2f", inputAmount)
                            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                        } else {
                            val toastMsg = String.format(
                                "Transferred $%.2f to account %s",
                                inputAmount,
                                inputAccountNumber
                            )
                            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                            val newBalance = balance - inputAmount
                            userViewModel.setBalance(newBalance)
                            findNavController().navigate(R.id.action_transferFundsFragment_to_userMenuFragment)
                        }
                    }
                }
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransferFundsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransferFundsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}