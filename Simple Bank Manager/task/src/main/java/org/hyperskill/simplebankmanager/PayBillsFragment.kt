package org.hyperskill.simplebankmanager

import android.app.AlertDialog
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
 * Use the [PayBillsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PayBillsFragment : Fragment() {
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
            findNavController().navigate(R.id.action_payBillsFragment_to_userMenuFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay_bills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.payBillsCodeInputEditText)
        val button = view.findViewById<Button>(R.id.payBillsShowBillInfoButton)

        val defaultBillInfoMap = mapOf(
            "ELEC" to Triple("Electricity", "ELEC", 45.0),
            "GAS" to Triple("Gas", "GAS", 20.0),
            "WTR" to Triple("Water", "WTR", 25.5)
        )

        val intent = (view.context as MainActivity).intent
        val billInfoMap = (intent.extras?.getSerializable("billInfo") ?: defaultBillInfoMap)
                as Map<String, Triple<String, String, Double>>

        button.setOnClickListener {
            if (!billInfoMap.containsKey(editText.text.toString())) {
                AlertDialog.Builder(context!!)
                    .setTitle("Error")
                    .setMessage("Wrong code")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                    }
                    .show()
            } else {
                var balance = userViewModel.balance.value ?: 100.0
                val triple = billInfoMap[editText.text.toString()]
                val message = String.format("Name: %s\nBillCode: %s\nAmount: $%.2f",
                    triple?.first, triple?.second, triple?.third)


                AlertDialog.Builder(context!!)
                    .setTitle("Bill info")
                    .setMessage(message)
                    .setPositiveButton("Confirm") { dialog, _ ->
                        if (balance >= triple?.third as Double) {
                            balance -= triple.third
                            userViewModel.setBalance(balance)
                            Toast.makeText(context, "Payment for bill ${triple.first}, was successful", Toast.LENGTH_SHORT).show()
                        } else {
                            dialog.dismiss()
                        }
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .setOnDismissListener {
                        AlertDialog.Builder(context!!)
                            .setTitle("Error")
                            .setMessage("Not enough funds")
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                    }
                    .show()

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
         * @return A new instance of fragment PayBillsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PayBillsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}