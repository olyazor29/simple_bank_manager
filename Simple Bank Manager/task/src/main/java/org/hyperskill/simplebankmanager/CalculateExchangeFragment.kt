package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalculateExchangeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalculateExchangeFragment : Fragment() {
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
            findNavController().navigate(R.id.action_calculateExchangeFragment_to_userMenuFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculate_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerFrom = view.findViewById<Spinner>(R.id.calculateExchangeFromSpinner)
        ArrayAdapter.createFromResource(requireContext(), R.array.currency, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerFrom.adapter = adapter
            }

        val spinnerTo = view.findViewById<Spinner>(R.id.calculateExchangeToSpinner)
        ArrayAdapter.createFromResource(requireContext(), R.array.currency, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTo.adapter = adapter
            }

        val itemListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (spinnerFrom.selectedItem.toString() == spinnerTo.selectedItem.toString()) {
                    Toast.makeText(context, "Cannot convert to same currency", Toast.LENGTH_SHORT).show()
                    val selectedPosition = spinnerTo.selectedItemPosition
                    spinnerTo.setSelection((selectedPosition + 1) % 3)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        spinnerFrom.onItemSelectedListener = itemListener
        spinnerTo.onItemSelectedListener = itemListener

        val defaultMap = mapOf(
            "EUR" to mapOf(
                "GBP" to 0.5,
                "USD" to 2.0
            ),
            "GBP" to mapOf(
                "EUR" to 2.0,
                "USD" to 4.0
            ),
            "USD" to mapOf(
                "EUR" to 0.5,
                "GBP" to 0.25
            )
        )

        val intent = (view.context as MainActivity).intent
        val exchangeMap = intent.extras?.getSerializable("exchangeMap")
                as? Map<String, Map<String, Double>> ?: defaultMap

        view.findViewById<Button>(R.id.calculateExchangeButton).setOnClickListener {
            val currencyFrom = spinnerFrom.selectedItem.toString()
            val currencyTo = spinnerTo.selectedItem.toString()

            if (view.findViewById<EditText>(R.id.calculateExchangeAmountEditText).text.isEmpty()) {
                Toast.makeText(context, "Enter amount",Toast.LENGTH_SHORT).show()
            } else {
                val amountToCalculate = view.findViewById<EditText>(R.id.calculateExchangeAmountEditText)
                    .text.toString().toDouble()

                val currencyCalculation = exchangeMap[currencyFrom]!![currencyTo]!!
                val result = amountToCalculate * currencyCalculation

                val symbolFrom = when (currencyFrom) {
                    "USD" -> "$"
                    "EUR" -> "€"
                    else -> "£"
                }

                val symbolTo = when (currencyTo) {
                    "USD" -> "$"
                    "EUR" -> "€"
                    else -> "£"
                }

                val resultString = String.format("$symbolFrom%.2f = $symbolTo%.2f",
                    amountToCalculate, result)

                view.findViewById<TextView>(R.id.calculateExchangeDisplayTextView)
                    .text = resultString

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
         * @return A new instance of fragment CalculateExchangeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalculateExchangeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}