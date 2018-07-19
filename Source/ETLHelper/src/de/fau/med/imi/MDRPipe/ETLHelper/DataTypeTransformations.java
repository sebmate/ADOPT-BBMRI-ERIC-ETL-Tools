package de.fau.med.imi.MDRPipe.ETLHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

public class DataTypeTransformations {
	
	private List<String> dataTypes;
	private List<String> successStates;
	private int[][] countedTransformations;
	private int[][][] countedTransformationsSuccess;
	private ArrayList<String> conversionErrors;
	private ArrayList<String> conversionWarnings;
	
	public DataTypeTransformations() {
		this.setDataTypes(Arrays.asList("BOOLEAN", "DATE", "DATETIME", "ENUMERATED", "FLOAT", "INTEGER", "STRING", "(unknown)"));
		this.setSuccessStates(Arrays.asList("ERROR", "WARNING", "OK"));
		this.setCountedTransformations(new int[this.getDataTypes().size()][this.getDataTypes().size()]);
		this.setCountedTransformationsSuccess(new int[this.getDataTypes().size()][this.getDataTypes().size()][this.getDataTypes().size()]);
		this.setConversionErrors(new ArrayList<String>());
		this.setConversionWarnings(new ArrayList<String>());
	}
	
	public void performTransformationOnETLResultEntry(ETLResultEntry etlResultEntry) {
		
		// COUNT THE TRANSFORMATION THAT WILL BE PERFORMED
		this.increaseSpecificTransformationCount(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType());
		
		switch (etlResultEntry.getSourceDataType()) {
			case "BOOLEAN": convertFromBoolean(etlResultEntry); break;
			case "DATE": convertFromDate(etlResultEntry); break;
			case "DATETIME": convertFromDateTime(etlResultEntry); break;
			case "ENUMERATED": convertFromEnumerated(etlResultEntry); break;
			case "INTEGER": convertFromInteger(etlResultEntry); break;
			case "FLOAT": convertFromFloat(etlResultEntry); break;
			case "STRING": convertFromString(etlResultEntry); break;
			default:
				if(MDRPipeConfiguration.getDebug()) {
					System.out.println("WARNING: Unknown data type " + etlResultEntry.getSourceDataType());
				}
				showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
				break;
		}
		
	}
	
	public void setSuccess(ETLResultEntry etlResultEntry, String success) {
		switch(success) {
			case "OK": etlResultEntry.setTransformationSuccess(2); break;
			case "WARNING": etlResultEntry.setTransformationSuccess(1); break;
			case "ERROR": etlResultEntry.setTransformationSuccess(0); break;
			default: etlResultEntry.setTransformationSuccess(0); break;
		}
		this.increaseSpecificTransformationSuccessCount(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry.getTransformationSuccess());
	}
	
	// ========================= Basic Data Type Conversions =========================

	private String normalizeEnumerated(String in) {
		in = in.trim();
		String returnValue = null;
		if (!in.equals(""))
			returnValue = in;
		return returnValue;
	}

	private String normalizeInteger(String in) {
		in = in.trim();//.replaceAll(",", ".");

		String returnValue = null;
		try {
			returnValue = "" + Integer.parseInt(in);
		} catch (Exception e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	private String normalizeFloat(String in) {
		in = in.trim().replaceAll(",", ".");
		String returnValue = null;
		try {
			returnValue = "" + Float.parseFloat(in);
		} catch (Exception e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	private String normalizeBoolean(String in) {
		in = in.trim().toUpperCase();
		String returnValue = null;
		if(in.equals("TRUE") || in.equals("YES") || in.equals("1"))
			returnValue = "TRUE";
		if(in.equals("FALSE") || in.equals("NO") || in.equals("0"))
			returnValue = "FALSE";
		return returnValue;
	}

	private String normalizeString(String in) {
		return in;
	}

	private String normalizeDate(String in, ETLResultEntry etlResultEntry) {

		ArrayList<String> allowedDateFormats = new ArrayList<String>();
		allowedDateFormats.add("yyyy-MM-dd");
		allowedDateFormats.add("dd/MM/yyyy");
		allowedDateFormats.add("MM.yyyy");
		allowedDateFormats.add("yyyy-MM");
		allowedDateFormats.add("dd.MM.yyyy");
		allowedDateFormats.add("dd.MM.yy");
		allowedDateFormats.add("yyyy.MM.dd");
		
		
		Date date = null;
		String dateString = null;

		for(int i = 0; i < allowedDateFormats.size(); i++) {
			try {
				DateFormat format = new SimpleDateFormat(allowedDateFormats.get(i), Locale.ENGLISH);
				if(date == null) {
					date = format.parse(in);
					String tempDateString = new SimpleDateFormat(allowedDateFormats.get(i)).format(date);
					if(in.equals(tempDateString)) {
						dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
						break;
					} else {
						date = null;
					}
				}
			} catch (ParseException e) {
				date = null;
				dateString = null;
				if(MDRPipeConfiguration.getDebug()) {
					e.printStackTrace();
				}
			}
		}
		if (date == null) {
			return null;
		} else {
			return dateString + "";
		}
	}
	
	// TODO
	private String normalizeDateTime(String in) {
		if(MDRPipeConfiguration.getDebug()) {
			System.out.println(" WARNING: Conversion for DateTime is not yet complete!");
		}
		return in;
	}
	
	private void convertFromBoolean(ETLResultEntry etlResultEntry) {
		if (normalizeBoolean(etlResultEntry.getDataValue()) != null) {
			switch(etlResultEntry.getTargetDataType()) {
				case "BOOLEAN":		this.convertFromBooleanToBoolean(etlResultEntry); break;
				case "DATE":		this.convertFromBooleanToDate(etlResultEntry); break;
				case "DATETIME":	this.convertFromBooleanToDateTime(etlResultEntry); break;
				case "ENUMERATED":	this.convertFromBooleanToEnumerated(etlResultEntry); break;
				case "FLOAT":		this.convertFromBooleanToFloat(etlResultEntry); break;
				case "INTEGER":		this.convertFromBooleanToInteger(etlResultEntry); break;
				case "STRING":		this.convertFromBooleanToString(etlResultEntry); break;
				default:			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry); break;
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromBooleanToBoolean(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(etlResultEntry.getTargetPermValue());
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromBooleanToDate(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromBooleanToDateTime(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromBooleanToEnumerated(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(etlResultEntry.getTargetPermValue());
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromBooleanToFloat(ETLResultEntry etlResultEntry) {
		if(etlResultEntry.getSourcePermValue().equals("TRUE")) {
			etlResultEntry.setFinalValue("1");
			this.setSuccess(etlResultEntry, "OK");
		} else if (etlResultEntry.getSourcePermValue().equals("FALSE")) {
			etlResultEntry.setFinalValue("0");
			this.setSuccess(etlResultEntry, "OK");
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromBooleanToInteger(ETLResultEntry etlResultEntry) {
		if(etlResultEntry.getSourcePermValue().equals("TRUE")) {
			etlResultEntry.setFinalValue("1");
			this.setSuccess(etlResultEntry, "OK");
		} else if (etlResultEntry.getSourcePermValue().equals("FALSE")) {
			etlResultEntry.setFinalValue("0");
			this.setSuccess(etlResultEntry, "OK");
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromBooleanToString(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(etlResultEntry.getSourcePermValue());
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromEnumerated(ETLResultEntry etlResultEntry) {
		if(normalizeEnumerated(etlResultEntry.getDataValue()) != null) {	
			switch(etlResultEntry.getTargetDataType()) {
				case "BOOLEAN": 	this.convertFromEnumeratedToBoolean(etlResultEntry); break;
				case "DATE":		this.convertFromEnumeratedToDate(etlResultEntry); break;
				case "DATETIME":	this.convertFromEnumeratedToDateTime(etlResultEntry); break;
				case "ENUMERATED":	this.convertFromEnumeratedToEnumerated(etlResultEntry); break;
				case "FLOAT":		this.convertFromEnumeratedToFloat(etlResultEntry); break;
				case "INTEGER":		this.convertFromEnumeratedToInteger(etlResultEntry); break;
				case "STRING":		this.convertFromEnumeratedToString(etlResultEntry); break;
				default:			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry); break;
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromEnumeratedToBoolean(ETLResultEntry etlResultEntry) {
		if (etlResultEntry.getTargetDataType().equals("BOOLEAN") && etlResultEntry.getTargetPermValue().equals("TRUE")) {
			etlResultEntry.setFinalValue("TRUE");
			this.setSuccess(etlResultEntry, "OK");
		} else if (etlResultEntry.getTargetDataType().equals("BOOLEAN") && etlResultEntry.getTargetPermValue().equals("FALSE")) {
			etlResultEntry.setFinalValue("FALSE");
			this.setSuccess(etlResultEntry, "OK");
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromEnumeratedToDate(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromEnumeratedToDateTime(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromEnumeratedToEnumerated(ETLResultEntry etlResultEntry) {
			etlResultEntry.setFinalValue(etlResultEntry.getTargetPermValue());
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromEnumeratedToFloat(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromEnumeratedToInteger(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromEnumeratedToString(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(etlResultEntry.getDataValue());
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromFloat(ETLResultEntry etlResultEntry) {
		if(normalizeFloat(etlResultEntry.getDataValue()) != null) {
			switch(etlResultEntry.getTargetDataType()) {
				case "BOOLEAN":		this.convertFromFloatToBoolean(etlResultEntry); break;
				case "DATE": 		this.convertFromFloatToDate(etlResultEntry); break;
				case "DATETIME":	this.convertFromFloatToDateTime(etlResultEntry); break;
				case "ENUMERATED": 	this.convertFromFloatToEnumerated(etlResultEntry); break;
				case "FLOAT":		this.convertFromFloatToFloat(etlResultEntry); break;
				case "INTEGER": 	this.convertFromFloatToInteger(etlResultEntry); break;
				case "STRING":		this.convertFromFloatToString(etlResultEntry); break;
				default: 			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry); break;
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromFloatToBoolean(ETLResultEntry etlResultEntry) {
		if(etlResultEntry.getTargetPermValue().equals("TRUE")) {
			if(normalizeBoolean(etlResultEntry.getDataValue()) != null) {
				etlResultEntry.setFinalValue(normalizeBoolean(etlResultEntry.getDataValue()));
				this.setSuccess(etlResultEntry, "OK");
			} else {
				etlResultEntry.setFinalValue("");
				showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
			}
		} else if(etlResultEntry.getTargetPermValue().equals("FALSE")) {
			if(normalizeBoolean(etlResultEntry.getDataValue()) != null) {
				etlResultEntry.setFinalValue(normalizeBoolean(etlResultEntry.getDataValue()));
				this.setSuccess(etlResultEntry, "OK");
			} else {
				etlResultEntry.setFinalValue("");
				showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
			}
		}
	}
	
	private void convertFromFloatToDate(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromFloatToDateTime(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromFloatToEnumerated(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromFloatToFloat(ETLResultEntry etlResultEntry) {
		if(normalizeFloat(etlResultEntry.getDataValue()) != null) {
			etlResultEntry.setFinalValue(normalizeFloat(etlResultEntry.getDataValue()));
			this.setSuccess(etlResultEntry, "OK");
		} else {
			etlResultEntry.setFinalValue("");
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromFloatToInteger(ETLResultEntry etlResultEntry) {
		if(normalizeFloat(etlResultEntry.getDataValue()) != null) {
			etlResultEntry.setFinalValue(normalizeInteger(etlResultEntry.getDataValue()));
			this.setSuccess(etlResultEntry, "OK");
		} else {
			etlResultEntry.setFinalValue("");
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromFloatToString(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(etlResultEntry.getDataValue());
		this.setSuccess(etlResultEntry, "OK");
	}

	private void convertFromInteger(ETLResultEntry etlResultEntry) {
		if(normalizeInteger(etlResultEntry.getDataValue()) != null) {
			switch(etlResultEntry.getTargetDataType()) {
				case "BOOLEAN":		this.convertFromIntegerToBoolean(etlResultEntry); break;
				case "DATE": 		this.convertFromIntegerToDate(etlResultEntry); break;
				case "DATETIME":	this.convertFromIntegerToDateTime(etlResultEntry); break;
				case "ENUMERATED": 	this.convertFromIntegerToEnumerated(etlResultEntry); break;
				case "FLOAT":		this.convertFromIntegerToFloat(etlResultEntry); break;
				case "INTEGER": 	this.convertFromIntegerToInteger(etlResultEntry); break;
				case "STRING":		this.convertFromIntegerToString(etlResultEntry); break;
				default: 			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromIntegerToBoolean(ETLResultEntry etlResultEntry) {
		if(etlResultEntry.getTargetPermValue().equals("TRUE")) {
			if (normalizeBoolean(etlResultEntry.getDataValue()) != null) {
				etlResultEntry.setFinalValue(normalizeBoolean(etlResultEntry.getDataValue()));
				this.setSuccess(etlResultEntry, "OK");
			} else {
				etlResultEntry.setFinalValue("");
				showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
			}
		} else if(etlResultEntry.getTargetPermValue().equals("FALSE")) {
			if (normalizeBoolean(etlResultEntry.getDataValue()) != null) {
				etlResultEntry.setFinalValue(normalizeBoolean(etlResultEntry.getDataValue()));
				this.setSuccess(etlResultEntry, "OK");
			} else {
				etlResultEntry.setFinalValue("");
				showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromIntegerToDate(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromIntegerToDateTime(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromIntegerToEnumerated(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromIntegerToFloat(ETLResultEntry etlResultEntry) {
		if(normalizeFloat(etlResultEntry.getDataValue()) != null) {
			etlResultEntry.setFinalValue(normalizeFloat(etlResultEntry.getDataValue()));
			this.setSuccess(etlResultEntry, "OK");
		} else {
			etlResultEntry.setFinalValue("");
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromIntegerToInteger(ETLResultEntry etlResultEntry) {
		if (normalizeInteger(etlResultEntry.getDataValue()) != null) {
			etlResultEntry.setFinalValue(normalizeInteger(etlResultEntry.getDataValue()));
			this.setSuccess(etlResultEntry, "OK");
		} else {
			etlResultEntry.setFinalValue("");
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromIntegerToString(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(etlResultEntry.getDataValue());
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromString(ETLResultEntry etlResultEntry) {
		if(normalizeString(etlResultEntry.getDataValue()) != null) {
			switch(etlResultEntry.getTargetDataType()) {
				case "BOOLEAN": this.convertFromStringToBoolean(etlResultEntry); break;
				case "DATE": 		this.convertFromStringToDate(etlResultEntry); break;
				case "DATETIME": 	this.convertFromStringToDateTime(etlResultEntry); break;
				case "ENUMERATED": 	this.convertFromStringToEnumerated(etlResultEntry); break;
				case "FLOAT": 		this.convertFromStringToFloat(etlResultEntry); break;
				case "INTEGER": 	this.convertFromStringToInteger(etlResultEntry); break;
				case "STRING": 		this.convertFromStringToString(etlResultEntry); break;
				default: showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry); break;
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}

	private void convertFromStringToBoolean(ETLResultEntry etlResultEntry) {
		if(etlResultEntry.getTargetPermValue().equals("TRUE")) {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		} else if(etlResultEntry.getTargetPermValue().equals("TRUE")) {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromStringToDate(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromStringToDateTime(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromStringToEnumerated(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromStringToFloat(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromStringToInteger(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromStringToString(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(etlResultEntry.getDataValue());
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromDate(ETLResultEntry etlResultEntry) {
		if(normalizeDate(etlResultEntry.getDataValue(), etlResultEntry) != null) {
			switch(etlResultEntry.getTargetDataType()) {
				case "BOOLEAN": 	this.convertFromDateToBoolean(etlResultEntry); break;
				case "DATE": 		this.convertFromDateToDate(etlResultEntry); break;
				case "DATETIME": 	this.convertFromDateToDateTime(etlResultEntry); break;
				case "ENUMERATED": 	this.convertFromDateToEnumerated(etlResultEntry); break;
				case "FLOAT": 		this.convertFromDateToFloat(etlResultEntry); break;
				case "INTEGER": 	this.convertFromDateToInteger(etlResultEntry); break;
				case "STRING": 		this.convertFromDateToString(etlResultEntry); break;
				default:			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry); break;
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}
	
	private void convertFromDateToBoolean(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateToDate(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(normalizeDate(etlResultEntry.getDataValue(), etlResultEntry));
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromDateToDateTime(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(normalizeDateTime(etlResultEntry.getDataValue()));
		showConversionWarning(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateToEnumerated(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateToFloat(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateToInteger(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateToString(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(normalizeString(etlResultEntry.getDataValue()));
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromDateTime(ETLResultEntry etlResultEntry) {
		if(normalizeDateTime(etlResultEntry.getDataValue()) != null) {
			switch(etlResultEntry.getTargetDataType()) {
				case "BOOLEAN":		this.convertFromDateTimeToBoolean(etlResultEntry); break;
				case "DATE":		this.convertFromDateTimeToDate(etlResultEntry); break;
				case "DATETIME":	this.convertFromDateTimeToDateTime(etlResultEntry); break;
				case "ENUMERATED":	this.convertFromDateTimeToEnumerated(etlResultEntry); break;
				case "FLOAT":		this.convertFromDateTimeToFloat(etlResultEntry); break;
				case "INTEGER":		this.convertFromDateTimeToInteger(etlResultEntry); break;
				case "STRING":		this.convertFromDateTimeToString(etlResultEntry); break;
				default:			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry); break;
			}
		} else {
			showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
		}
	}

	private void convertFromDateTimeToBoolean(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateTimeToDate(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(normalizeDateTime(etlResultEntry.getDataValue()));
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void convertFromDateTimeToDateTime(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(normalizeDateTime(etlResultEntry.getDataValue()));
		showConversionWarning(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateTimeToEnumerated(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateTimeToFloat(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateTimeToInteger(ETLResultEntry etlResultEntry) {
		showConversionError(etlResultEntry.getSourceDataType(), etlResultEntry.getTargetDataType(), etlResultEntry);
	}
	
	private void convertFromDateTimeToString(ETLResultEntry etlResultEntry) {
		etlResultEntry.setFinalValue(normalizeString(etlResultEntry.getDataValue()));
		this.setSuccess(etlResultEntry, "OK");
	}
	
	private void showConversionWarning(String sourceDataType, String targetDataType, ETLResultEntry etlResultEntry) {
		if(MDRPipeConfiguration.getDebug()) {
			System.out.println(" WARNING: Converting from \"" + sourceDataType + "\" to " + targetDataType + "!");
		}
		if(!etlResultEntry.getTargetDataType().equals("(unknown)")) {
			this.getConversionWarnings().add(etlResultEntry.getDataConcept() + ": " + etlResultEntry.getDataValue());
		}
		this.setSuccess(etlResultEntry, "WARNING");
	}

	private void showConversionError(String sourceDataType, String targetDataType, ETLResultEntry etlResultEntry) {
		if(MDRPipeConfiguration.getDebug()) {
			System.out.println(" ERROR: Cannot convert from \"" + sourceDataType + "\" to " + targetDataType + "!");
		}
		if(!etlResultEntry.getTargetDataType().equals("(unknown)")) {
			//this.getConversionErrors().add(etlResultEntry.getDataConcept() + ": " + etlResultEntry.getDataValue());
			
			this.getConversionErrors().add(etlResultEntry.getSourceDataType() + " => " + etlResultEntry.getTargetDataType() + ": " + etlResultEntry.getDataConcept() + " = " + etlResultEntry.getDataValue() + " => " + etlResultEntry.getTargetPath());
			
		}
		this.setSuccess(etlResultEntry, "ERROR");
	}
	
	public void showDataTypeTransformations() {
			
		System.out.println("\n  The following data type castings (from TYPE => TYPE) were applied:\n");
				
	
		for(int i = 0; i < this.getCountedTransformations().length - 1; i++) {
			for(int j = 0; j < this.getCountedTransformations()[i].length - 1; j++) {
				String fromDataType = this.getDataTypes().get(i);
				String toDataType = this.getDataTypes().get(j);
				int count = this.getSpecificTransformationCount(fromDataType, toDataType);
				if(count != 0) {
					System.out.print("\t" + fromDataType + " => " + toDataType + ": " + count + " (");
					for(int k = 0; k < this.getSuccessStates().size(); k++) {
						System.out.print(this.getSuccessStates().get(k) + ": " + this.getSpecificTransformationSuccessCount(fromDataType, toDataType, k));
						if(k < this.getSuccessStates().size()-1) {
							System.out.print("; ");
						}
					}
					System.out.println(")");
				}
			}
		}
		
		
		// Uncomment this block to output statistics that can be easier used in Excel:

		/*

		System.out.println("\n  The following data type castings (from TYPE => TYPE) were applied (OK/WARNING/ERROR):\n");
		
		
		String from = "", to = "";
		for (int f = 0; f < 7; f++) {
			switch (f) {
			case 0:
				from = "ENUMERATED";
				break;
			case 1:
				from = "INTEGER";
				break;
			case 2:
				from = "FLOAT";
				break;
			case 3:
				from = "BOOLEAN";
				break;
			case 4:
				from = "STRING";
				break;
			case 5:
				from = "DATE";
				break;
			case 6:
				from = "DATETIME";
				break;
			}
			
			for (int t = 0; t < 7; t++) {

				switch (t) {
				case 0:
					to = "ENUMERATED";
					break;
				case 1:
					to = "INTEGER";
					break;
				case 2:
					to = "FLOAT";
					break;
				case 3:
					to = "BOOLEAN";
					break;
				case 4:
					to = "STRING";
					break;
				case 5:
					to = "DATE";
					break;
				case 6:
					to = "DATETIME";
					break;
				}

				System.out.print("\t" + from + " => " + to + ":\t");
				
				for(int i = 0; i < this.getCountedTransformations().length - 1; i++) {
					for(int j = 0; j < this.getCountedTransformations()[i].length - 1; j++) {
						String fromDataType = this.getDataTypes().get(i);
						String toDataType = this.getDataTypes().get(j);
						int count = this.getSpecificTransformationCount(fromDataType, toDataType);
						if(count != 0) {
							if (fromDataType.equals(from) && toDataType.equals(to)) {
								System.out.print(this.getSpecificTransformationSuccessCount(fromDataType, toDataType, 2));
								System.out.print("/");
								System.out.print(this.getSpecificTransformationSuccessCount(fromDataType, toDataType, 1));
								System.out.print("/");
								System.out.print(this.getSpecificTransformationSuccessCount(fromDataType, toDataType, 0));
							}
						}
					}
				}
				
				System.out.println("");
			}
		}
		
		*/
		
		if(this.getConversionErrors().size() > 0) {
			
			System.out.println("");
			System.out.println("  The following data type castings triggered errors:\n");
			
			
			//https://stackoverflow.com/a/203992
				
			List<String> al = this.getConversionErrors();
			Set<String> hs = new HashSet<>();
			hs.addAll(al);
			al.clear();
			al.addAll(hs);	
			for(int i = 0; i < al.size(); i++) {
				System.out.println("\t" + al.get(i));
			}
			
		}
		
		if(this.getConversionWarnings().size() > 0) {
			
			System.out.println("");
			
			System.out.println("=== Information about the warnings ===");
			
			for(int i = 0; i < this.getConversionWarnings().size(); i++) {
				System.out.println("\t" + this.getConversionWarnings().get(i));
			}
			
		}
		
		System.out.println("");
		
		System.out.println("=== Information about the used transformation rules ===");
		
		int numberOfTransformationsRulesUsed = 0;
		System.out.print("Number of transformation rules used: ");
		// until length - 1 for skipping the UNKNOWN data type
		for(int i = 0; i < this.getCountedTransformations().length - 1; i++) {
			// until length - 1 for skipping the UNKNOWN data type
			for(int j = 0; j < this.getCountedTransformations()[i].length - 1; j++) {
				String fromDataType = this.getDataTypes().get(i);
				String toDataType = this.getDataTypes().get(j);
				numberOfTransformationsRulesUsed = numberOfTransformationsRulesUsed + this.getSpecificTransformationCount(fromDataType, toDataType);
			}
		}
		System.out.println(numberOfTransformationsRulesUsed);
		
		System.out.println("Number of warnings: " + this.countNumberOfTranslationRulesBySuccess("WARNING"));
		System.out.println("Number of errors: " + this.countNumberOfTranslationRulesBySuccess("ERROR"));
		
		int numberOfOK = this.countNumberOfTranslationRulesBySuccess("OK");
		System.out.println("Number of good transformations: " + numberOfOK);
		
		double percent = ((double) numberOfOK / (double) numberOfTransformationsRulesUsed) * 100.0;
		
		System.out.println("");
		
		System.out.println("  => " + ETLHelper.round(percent, 2) + "% of the data records that do have a mapping could be transformed.");
		
	}
	
	private int getDataTypeIndex(String dataTypeName) {
		return this.getDataTypes().indexOf(dataTypeName);
	}
	
	public int getSpecificTransformationCount(String from, String to) {
		int fromIndex = this.getDataTypeIndex(from);
		int toIndex = this.getDataTypeIndex(to);
		return this.getCountedTransformations()[fromIndex][toIndex];
	}
	
	public void increaseSpecificTransformationCount(String from, String to) {
		int fromIndex = this.getDataTypeIndex(from);
		int toIndex = this.getDataTypeIndex(to);
		int currentTransformationCount = this.getSpecificTransformationCount(from, to);
		int increasedTransformationCount = currentTransformationCount + 1;
		this.getCountedTransformations()[fromIndex][toIndex] = increasedTransformationCount;
	}
	
	public int getSpecificTransformationSuccessCount(String from, String to, int success) {
		int fromIndex = this.getDataTypeIndex(from);
		int toIndex = this.getDataTypeIndex(to);
		return this.getCountedTransformationsSuccess()[fromIndex][toIndex][success];
	}
	
	public void increaseSpecificTransformationSuccessCount(String from, String to, int success) {
		int fromIndex = this.getDataTypeIndex(from);
		int toIndex = this.getDataTypeIndex(to);
		int currentTransformationSuccessCount = this.getSpecificTransformationSuccessCount(from, to, success);
		int increasedTransformationSuccessCount = currentTransformationSuccessCount + 1;
		this.getCountedTransformationsSuccess()[fromIndex][toIndex][success] = increasedTransformationSuccessCount;
	}
	
	public int countNumberOfTranslationRulesBySuccess(String translationRuleSuccess) {
		int numberOfTranslationRulesBySuccess = 0;
		// until length - 1 for skipping the UNKNOWN data type
		for(int i = 0; i < this.getCountedTransformations().length - 1; i++) {
			// until length - 1 for skipping the UNKNOWN data type
			for(int j = 0; j < this.getCountedTransformations()[i].length - 1; j++) {
				String fromDataType = this.getDataTypes().get(i);
				String toDataType = this.getDataTypes().get(j);
				for(int k = 0; k < this.getSuccessStates().size(); k++) {
					if(this.getSuccessStates().get(k).equals(translationRuleSuccess)) {
						numberOfTranslationRulesBySuccess = numberOfTranslationRulesBySuccess + this.getSpecificTransformationSuccessCount(fromDataType, toDataType, k);
					}
				}
			}
		}
		return numberOfTranslationRulesBySuccess;
	}
	
	public List<String> getDataTypes() {
		return dataTypes;
	}
	
	public void setDataTypes(List<String> dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	public int[][] getCountedTransformations() {
		return countedTransformations;
	}
	
	public void setCountedTransformations(int[][] countedTransformations) {
		this.countedTransformations = countedTransformations;
	}
	
	public int[][][] getCountedTransformationsSuccess() {
		return countedTransformationsSuccess;
	}
	
	public void setCountedTransformationsSuccess(int[][][] countedTransformationsSuccess) {
		this.countedTransformationsSuccess = countedTransformationsSuccess;
	}
	
	public List<String> getSuccessStates() {
		return successStates;
	}
	
	public void setSuccessStates(List<String> successStates) {
		this.successStates = successStates;
	}

	public ArrayList<String> getConversionErrors() {
		return conversionErrors;
	}

	public void setConversionErrors(ArrayList<String> conversionErrors) {
		this.conversionErrors = conversionErrors;
	}

	public ArrayList<String> getConversionWarnings() {
		return conversionWarnings;
	}

	public void setConversionWarnings(ArrayList<String> conversionWarnings) {
		this.conversionWarnings = conversionWarnings;
	}
	
}
