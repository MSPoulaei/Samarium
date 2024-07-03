package ir.tehranshomal.samarium.logic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoTdscdma
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import ir.tehranshomal.samarium.model.Point


    fun getServingCellParameters(context: Context): Point? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            return null
        }
        val point=Point()

        val cellInfo: List<CellInfo>? = telephonyManager.allCellInfo
        point.technologyName=readNetworkType(telephonyManager.networkType)
        val gen=point.technologyName.substring(0,1)
        try {
            point.technologyNumber=gen.toInt()
        }catch (_:NumberFormatException){

        }
        point.MCC=telephonyManager.simOperator.substring(0,3)
        point.MNC=telephonyManager.simOperator.substring(3)
//        point.PLMNID=telephonyManager.networkOperator.toString()
        point.PLMNID=point.MCC+point.MNC
        cellInfo?.forEach { info ->
            if (!info.isRegistered) return@forEach
            when (info) {
                is CellInfoGsm -> {
                    val cellIdentityGsm = info.cellIdentity
                    val cellSignalStrengthGsm = info.cellSignalStrength
//                    println("GSM Cell:\n")
                    point.cellId=cellIdentityGsm.cid
                    point.LAC=cellIdentityGsm.lac
                    point.MCC=cellIdentityGsm.mcc.toString()
                    point.MNC=cellIdentityGsm.mnc.toString()
                    point.PLMNID=point.MCC+point.MNC
                    point.signalStrength=cellSignalStrengthGsm.dbm
                    //TODO: quality gsm
                    point.signalQuality=cellSignalStrengthGsm.level
                }
                is CellInfoCdma -> {
                    val cellIdentityCdma = info.cellIdentity
                    val cellSignalStrengthCdma = info.cellSignalStrength
//                    println("WCDMA Cell:")
                    point.cellId=cellIdentityCdma.basestationId
//                    point.LAC=cellIdentityCdma.lac
                    point.technologyNumber=3
                    point.signalStrength=cellSignalStrengthCdma.dbm //RSCP
                    point.signalQuality=cellSignalStrengthCdma.cdmaEcio
                }
                is CellInfoWcdma -> {
                    val cellIdentityWcdma = info.cellIdentity
                    val cellSignalStrengthWcdma = info.cellSignalStrength
//                    println("WCDMA Cell:")
                    point.cellId=cellIdentityWcdma.cid
                    point.LAC=cellIdentityWcdma.lac
                    point.MCC=cellIdentityWcdma.mcc.toString()
                    point.MNC=cellIdentityWcdma.mnc.toString()
                    point.PLMNID=point.MCC+point.MNC
                    point.technologyNumber=3
//                    println("PSC: ${cellIdentityWcdma.psc}\n")
                    point.signalStrength=cellSignalStrengthWcdma.dbm //RSCP
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        point.signalQuality=cellSignalStrengthWcdma.ecNo
                    }
                    else{
                        point.signalQuality=cellSignalStrengthWcdma.level
                    }
                }
                is CellInfoLte -> {
                    val cellIdentityLte = info.cellIdentity

                    val cellSignalStrengthLte = info.cellSignalStrength
//                    println("LTE Cell:")
                    point.cellId=cellIdentityLte.ci
                    point.LAC=cellIdentityLte.tac
                    point.MCC=cellIdentityLte.mcc.toString()
                    point.MNC=cellIdentityLte.mnc.toString()
                    point.PLMNID=point.MCC+point.MNC
                    point.technologyNumber=4
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        point.signalStrength=cellSignalStrengthLte.rsrp
                        point.signalQuality=cellSignalStrengthLte.rsrq
                    }
                    else{
                        point.signalStrength=cellSignalStrengthLte.dbm
                        point.signalQuality=cellSignalStrengthLte.level
                    }
//                    println("PSC: ${cellIdentityWcdma.psc}\n")
//                    println("PCI: ${cellIdentityLte.pci}\n")
                }
                // For future compatibility or unknown types
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        when (info) {
                            is CellInfoNr -> {
                                val cellIdentityNr = info.cellIdentity
                                val cellSignalStrengthNr = info.cellSignalStrength
//                                point.cellId=

//                                point.LAC=cellIdentityNr.tac
//                                point.MCC=cellIdentityNr.mcc.toString()
//                                point.MNC=cellIdentityNr.mnc.toString()
                                point.signalStrength=cellSignalStrengthNr.dbm
                                point.signalQuality=cellSignalStrengthNr.level
                                point.technologyNumber=5

                                println("NR (5G) Cell:")
//                            println("NCI: ${cellIdentityNr.nci}\n")
//                            println("TAC: ${cellIdentityNr.tac}")
//                            println("MCC: ${cellIdentityNr.mcc}")
//                            println("MNC: ${cellIdentityNr.mnc}")
//                            println("PCI: ${cellIdentityNr.pci}")
//                            println("Signal Strength: ${cellSignalStrengthNr.dbm} dBm")
                            }
                            is CellInfoTdscdma -> {
                                val cellIdentityTdscdma = info.cellIdentity
                                val cellSignalStrengthTdscdma = info.cellSignalStrength
                                point.cellId=cellIdentityTdscdma.cid
                                point.technologyNumber=3
                                point.LAC=cellIdentityTdscdma.lac
                                point.signalStrength=cellSignalStrengthTdscdma.rscp
                                point.signalQuality=cellSignalStrengthTdscdma.level

//                                println("TD-SCDMA Cell:")
//                                println("CPID: ${cellIdentityTdscdma.cpid}\n")
                            }
                            else->{
                                println("Unknown or Unsupported Cell Type:")
                                println("Cell Type: ${info.javaClass.simpleName}\n")
                                point.signalStrength=info.cellSignalStrength.dbm
                                point.signalQuality=info.cellSignalStrength.level
                            }
                        }
                    }
                }
            }

            // Common information for all cell types

//            result+=("Registered: ${info.isRegistered}\n")
//            result+=("Timestamp: ${info.timeStamp}\n")
//            result+=("-------------------------\n")

            return point
        }
//        return cellInfo
        return null
    }

    fun getServingCellParametersString(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            return "nadariiii"
        }

        val cellInfo: List<CellInfo>? = telephonyManager.allCellInfo
        var result="${readNetworkType(telephonyManager.networkType)}\n" +
                "initee ${cellInfo?.count()}\n"
        result+="networkOperatorName:${telephonyManager.networkOperatorName}\n"
        result+="plmnid:${telephonyManager.simOperator}\n"
        result+="networkOperator:${telephonyManager.networkOperator}\n"
        cellInfo?.forEach { info ->
            if (!info.isRegistered) return@forEach
            when (info) {
                is CellInfoGsm -> {

                    val cellIdentityGsm = info.cellIdentity
                    val cellSignalStrengthGsm = info.cellSignalStrength
                    println("GSM Cell:\n")
                    result+="GSM Cell:\n"
                    println("CID: ${cellIdentityGsm.cid}\n")
                    result+="CID: ${cellIdentityGsm.cid}\n"
                    println("LAC: ${cellIdentityGsm.lac}\n")
                    result+="LAC: ${cellIdentityGsm.lac}\n"
                    println("MCC: ${cellIdentityGsm.mcc}\n")
                    result+="MCC: ${cellIdentityGsm.mcc}\n"
                    println("MNC: ${cellIdentityGsm.mnc}\n")
                    result+="MNC: ${cellIdentityGsm.mnc}\n"
                    println("Signal Strength: ${cellSignalStrengthGsm.dbm} dBm\n")
                    result+="Signal Strength: ${cellSignalStrengthGsm.dbm} dBm\n"
                }
                is CellInfoLte -> {
                    val cellIdentityLte = info.cellIdentity

                    val cellSignalStrengthLte = info.cellSignalStrength
                    println("LTE Cell:")
                    result+="LTE Cell:"
                    println("CI: ${cellIdentityLte.ci}\n")
                    result+="CI: ${cellIdentityLte.ci}\n"
                    println("TAC: ${cellIdentityLte.tac}\n")
                    result+="TAC: ${cellIdentityLte.tac}\n"
                    println("MCC: ${cellIdentityLte.mcc}\n")
                    result+="MCC: ${cellIdentityLte.mcc}\n"
                    println("MNC: ${cellIdentityLte.mnc}\n")
                    result+="MNC: ${cellIdentityLte.mnc}\n"
                    println("PCI: ${cellIdentityLte.pci}\n")
                    result+="PCI: ${cellIdentityLte.pci}\n"
                    println("Signal Strength: ${cellSignalStrengthLte.dbm} dBm\n")
                    result+="Signal Strength: ${cellSignalStrengthLte.dbm} dBm\n"
                }
                is CellInfoWcdma -> {
                    val cellIdentityWcdma = info.cellIdentity
                    val cellSignalStrengthWcdma = info.cellSignalStrength
                    println("WCDMA Cell:")
                    result+="WCDMA Cell:"
                    println("CID: ${cellIdentityWcdma.cid}\n")
                    result+=("CID: ${cellIdentityWcdma.cid}\n")
                    println("LAC: ${cellIdentityWcdma.lac}\n")
                    result+=("LAC: ${cellIdentityWcdma.lac}\n")
                    println("MCC: ${cellIdentityWcdma.mcc}\n")
                    result+=("MCC: ${cellIdentityWcdma.mcc}\n")
                    println("MNC: ${cellIdentityWcdma.mnc}\n")
                    result+=("MNC: ${cellIdentityWcdma.mnc}\n")
                    println("PSC: ${cellIdentityWcdma.psc}\n")
                    result+=("PSC: ${cellIdentityWcdma.psc}\n")
                    println("Signal Strength: ${cellSignalStrengthWcdma.dbm} dBm\n")
                    result+=("Signal Strength: ${cellSignalStrengthWcdma.dbm} dBm\n")
                }
                // Add more cases for other cell types if needed

                // For future compatibility or unknown types
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        when (info) {
                            is CellInfoNr -> {
                                val cellIdentityNr = info.cellIdentity
                                val cellSignalStrengthNr = info.cellSignalStrength
                                println("NR (5G) Cell:")
                                result+=("NR (5G) Cell:\n")
//                            println("NCI: ${cellIdentityNr.nci}\n")
//                            result+=("NCI: ${cellIdentityNr.nci}\n")
//                            println("TAC: ${cellIdentityNr.tac}")
//                            result+=("TAC: ${cellIdentityNr.tac}")
//                            println("MCC: ${cellIdentityNr.mcc}")
//                            result+=("MCC: ${cellIdentityNr.mcc}")
//                            println("MNC: ${cellIdentityNr.mnc}")
//                            result+=("MNC: ${cellIdentityNr.mnc}")
//                            println("PCI: ${cellIdentityNr.pci}")
//                            result+=("PCI: ${cellIdentityNr.pci}")
                                println("Signal Strength: ${cellSignalStrengthNr.dbm} dBm")
                                result+=("Signal Strength: ${cellSignalStrengthNr.dbm} dBm\n")
                            }
                            is CellInfoTdscdma -> {
                                val cellIdentityTdscdma = info.cellIdentity
                                val cellSignalStrengthTdscdma = info.cellSignalStrength
                                println("TD-SCDMA Cell:")
                                result+=("TD-SCDMA Cell:")
                                println("CID: ${cellIdentityTdscdma.cid}\n")
                                result+=("CID: ${cellIdentityTdscdma.cid}\n")
                                println("LAC: ${cellIdentityTdscdma.lac}\n")
                                result+=("LAC: ${cellIdentityTdscdma.lac}\n")
//                            println("MCC: ${cellIdentityTdscdma.mcc}\n")
//                            println("MNC: ${cellIdentityTdscdma.mnc}\n")
                                println("CPID: ${cellIdentityTdscdma.cpid}\n")
                                result+=("CPID: ${cellIdentityTdscdma.cpid}\n")
                                println("Signal Strength: ${cellSignalStrengthTdscdma.dbm} dBm\n")
                                result+=("Signal Strength: ${cellSignalStrengthTdscdma.dbm} dBm\n")
                            }

                        }
                    }
                    println("Unknown or Unsupported Cell Type:")
                    result+=("Unknown or Unsupported Cell Type:")
                    println("Cell Type: ${info.javaClass.simpleName}\n")
                    result+=("Cell Type: ${info.javaClass.simpleName}\n")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        println("Cell Identity: ${info.cellIdentity}\n")
                        result+=("Cell Identity: ${info.cellIdentity}\n")
                        println("Signal Strength: ${info.cellSignalStrength}\n")
                        result+=("Signal Strength: ${info.cellSignalStrength}\n")
                    }
                }
            }

            // Common information for all cell types
            result+=("Registered: ${info.isRegistered}\n")
            result+=("Timestamp: ${info.timeStamp}\n")
            result+=("-------------------------\n")

        }
        return result
//        return cellInfo
    }


    private fun readNetworkType(networkType: Int): String {
        when (networkType) {
            TelephonyManager.NETWORK_TYPE_EDGE -> return "2G EDGE"
            TelephonyManager.NETWORK_TYPE_GPRS -> return "2G GPRS"
            TelephonyManager.NETWORK_TYPE_HSDPA -> return "3G HSDPA"
            TelephonyManager.NETWORK_TYPE_HSPA -> return "3G HSPA"
            TelephonyManager.NETWORK_TYPE_HSPAP -> return "3G HSPA+"
            TelephonyManager.NETWORK_TYPE_HSUPA -> return "3G HSUPA"
            TelephonyManager.NETWORK_TYPE_UMTS -> return "3G UMTS"
            TelephonyManager.NETWORK_TYPE_1xRTT -> return "3G 1xRTT"
            TelephonyManager.NETWORK_TYPE_CDMA -> return "3G CDMA"
            TelephonyManager.NETWORK_TYPE_EHRPD -> return "3G eHRPD"
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> return "3G TD-SCDMA"
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> return "3G EVDO rev. 0"
            TelephonyManager.NETWORK_TYPE_EVDO_A -> return "3G EVDO rev. A"
            TelephonyManager.NETWORK_TYPE_EVDO_B -> return "3G EVDO rev. B"
            TelephonyManager.NETWORK_TYPE_IDEN -> return "4G iDen"
            TelephonyManager.NETWORK_TYPE_LTE -> return "4G LTE"
            TelephonyManager.NETWORK_TYPE_NR -> return "5G NR"
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> return "Unknown"
        }
        throw RuntimeException("New type of network")
    }
