//      		----------------------------------------------------------
//      		Copyright (c) 2017, Battelle Memorial Institute
//      		All rights reserved.
//      		----------------------------------------------------------

// package gov.pnnl.gridlabd.cim ;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

// import gov.pnnl.gridlabd.cim.GldNode;

/**
 * <p>This class runs an example SQARQL query against Blazegraph triple-store</p> 
 *      
 * <p>Invoke as a console-mode program</p> 
 *      
 * @see CIMImporter#main 
 *      
 * @author Tom McDermott 
 * @version 1.0 
 *      
 */

public class CIMImporter extends Object {
	static HashMap<String,GldNode> mapNodes = new HashMap<>();
	static HashMap<String,DistBaseVoltage> mapBaseVoltages = new HashMap<>();
	static HashMap<String,DistCapacitor> mapCapacitors = new HashMap<>();
	static HashMap<String,DistConcentricNeutralCable> mapCNCables = new HashMap<>();
	static HashMap<String,DistCoordinates> mapCoordinates = new HashMap<>();
	static HashMap<String,DistLinesCodeZ> mapLinesCodeZ = new HashMap<>();
	static HashMap<String,DistLinesInstanceZ> mapLinesInstanceZ = new HashMap<>();
	static HashMap<String,DistLineSpacing> mapSpacings = new HashMap<>();
	static HashMap<String,DistLinesSpacingZ> mapLinesSpacingZ = new HashMap<>();
	static HashMap<String,DistLoad> mapLoads = new HashMap<>();
	static HashMap<String,DistOverheadWire> mapWires = new HashMap<>();
	static HashMap<String,DistPhaseMatrix> mapPhaseMatrices = new HashMap<>();
	static HashMap<String,DistPowerXfmrCore> mapXfmrCores = new HashMap<>();
	static HashMap<String,DistPowerXfmrMesh> mapXfmrMeshes = new HashMap<>();
	static HashMap<String,DistPowerXfmrWinding> mapXfmrWindings = new HashMap<>();
	static HashMap<String,DistRegulator> mapRegulators = new HashMap<>();
	static HashMap<String,DistSequenceMatrix> mapSequenceMatrices = new HashMap<>();
	static HashMap<String,DistSubstation> mapSubstations = new HashMap<>();
	static HashMap<String,DistSwitch> mapSwitches = new HashMap<>();
	static HashMap<String,DistTapeShieldCable> mapTSCables = new HashMap<>();
	static HashMap<String,DistXfmrCodeOCTest> mapCodeOCTests = new HashMap<>();
	static HashMap<String,DistXfmrCodeRating> mapCodeRatings = new HashMap<>();
	static HashMap<String,DistXfmrCodeSCTest> mapCodeSCTests = new HashMap<>();
	static HashMap<String,DistXfmrTank> mapTanks = new HashMap<>();

	static void LoadBaseVoltages() {
		ResultSet results = DistComponent.RunQuery (DistBaseVoltage.szQUERY);
		while (results.hasNext()) {
			DistBaseVoltage obj = new DistBaseVoltage (results);
			mapBaseVoltages.put (obj.GetKey(), obj);
		}
	}

	static void LoadSubstations() {
		ResultSet results = DistComponent.RunQuery (DistSubstation.szQUERY);
		while (results.hasNext()) {
			DistSubstation obj = new DistSubstation (results);
			mapSubstations.put (obj.GetKey(), obj);
			mapNodes.put (obj.bus, new GldNode(obj.bus));
			GldNode nd = mapNodes.get (obj.bus);
			nd.bSwing = true;
		}
	}

	static void LoadCapacitors() {
		ResultSet results = DistComponent.RunQuery (DistCapacitor.szQUERY);
		while (results.hasNext()) {
			DistCapacitor obj = new DistCapacitor (results);
			mapCapacitors.put (obj.GetKey(), obj);
		}
	}

	static void LoadLoads() {
		ResultSet results = DistComponent.RunQuery (DistLoad.szQUERY);
		while (results.hasNext()) {
			DistLoad obj = new DistLoad (results);
			mapLoads.put (obj.GetKey(), obj);
		}
	}

	static void LoadPhaseMatrices() {
		ResultSet results = DistComponent.RunQuery (DistPhaseMatrix.szQUERY);
		while (results.hasNext()) {
			DistPhaseMatrix obj = new DistPhaseMatrix (results);
			mapPhaseMatrices.put (obj.GetKey(), obj);
		}
	}

	static void LoadSequenceMatrices() {
		ResultSet results = DistComponent.RunQuery (DistSequenceMatrix.szQUERY);
		while (results.hasNext()) {
			DistSequenceMatrix obj = new DistSequenceMatrix (results);
			mapSequenceMatrices.put (obj.GetKey(), obj);
		}
	}

	static void LoadXfmrCodeRatings() {
		ResultSet results = DistComponent.RunQuery (DistXfmrCodeRating.szQUERY);
		while (results.hasNext()) {
			DistXfmrCodeRating obj = new DistXfmrCodeRating (results);
			mapCodeRatings.put (obj.GetKey(), obj);
		}
	}

	static void LoadXfmrCodeOCTests() {
		ResultSet results = DistComponent.RunQuery (DistXfmrCodeOCTest.szQUERY);
		while (results.hasNext()) {
			DistXfmrCodeOCTest obj = new DistXfmrCodeOCTest (results);
			mapCodeOCTests.put (obj.GetKey(), obj);
		}
	}

	static void LoadXfmrCodeSCTests() {
		ResultSet results = DistComponent.RunQuery (DistXfmrCodeSCTest.szQUERY);
		while (results.hasNext()) {
			DistXfmrCodeSCTest obj = new DistXfmrCodeSCTest (results);
			mapCodeSCTests.put (obj.GetKey(), obj);
		}
	}

	static void LoadPowerXfmrCore() {
		ResultSet results = DistComponent.RunQuery (DistPowerXfmrCore.szQUERY);
		while (results.hasNext()) {
			DistPowerXfmrCore obj = new DistPowerXfmrCore (results);
			mapXfmrCores.put (obj.GetKey(), obj);
		}
	}

	static void LoadPowerXfmrMesh() {
		ResultSet results = DistComponent.RunQuery (DistPowerXfmrMesh.szQUERY);
		while (results.hasNext()) {
			DistPowerXfmrMesh obj = new DistPowerXfmrMesh (results);
			mapXfmrMeshes.put (obj.GetKey(), obj);
		}
	}

	static void LoadOverheadWires() {
		ResultSet results = DistComponent.RunQuery (DistOverheadWire.szQUERY);
		while (results.hasNext()) {
			DistOverheadWire obj = new DistOverheadWire (results);
			mapWires.put (obj.GetKey(), obj);
		}
	}

	static void LoadTapeShieldCables() {
		ResultSet results = DistComponent.RunQuery (DistTapeShieldCable.szQUERY);
		while (results.hasNext()) {
			DistTapeShieldCable obj = new DistTapeShieldCable (results);
			mapTSCables.put (obj.GetKey(), obj);
		}
	}

	static void LoadConcentricNeutralCables() {
		ResultSet results = DistComponent.RunQuery (DistConcentricNeutralCable.szQUERY);
		while (results.hasNext()) {
			DistConcentricNeutralCable obj = new DistConcentricNeutralCable (results);
			mapCNCables.put (obj.GetKey(), obj);
		}
	}

	static void LoadLineSpacings() {
		ResultSet results = DistComponent.RunQuery (DistLineSpacing.szQUERY);
		while (results.hasNext()) {
			DistLineSpacing obj = new DistLineSpacing (results);
			mapSpacings.put (obj.GetKey(), obj);
		}
	}

	static void LoadSwitches() {
		ResultSet results = DistComponent.RunQuery (DistSwitch.szQUERY);
		while (results.hasNext()) {
			DistSwitch obj = new DistSwitch (results);
			mapSwitches.put (obj.GetKey(), obj);
		}
	}

	static void LoadLinesInstanceZ() {
		ResultSet results = DistComponent.RunQuery (DistLinesInstanceZ.szQUERY);
		while (results.hasNext()) {
			DistLinesInstanceZ obj = new DistLinesInstanceZ (results);
			mapLinesInstanceZ.put (obj.GetKey(), obj);
		}
	}

	static void LoadLinesCodeZ() {
		ResultSet results = DistComponent.RunQuery (DistLinesCodeZ.szQUERY);
		while (results.hasNext()) {
			DistLinesCodeZ obj = new DistLinesCodeZ (results);
			mapLinesCodeZ.put (obj.GetKey(), obj);
		}
	}

	static void LoadLinesSpacingZ() {
		ResultSet results = DistComponent.RunQuery (DistLinesSpacingZ.szQUERY);
		while (results.hasNext()) {
			DistLinesSpacingZ obj = new DistLinesSpacingZ (results);
			mapLinesSpacingZ.put (obj.GetKey(), obj);
		}
	}

	static void LoadRegulators() {
		ResultSet results = DistComponent.RunQuery (DistRegulator.szQUERY);
		while (results.hasNext()) {
			DistRegulator obj = new DistRegulator (results);
			mapRegulators.put (obj.GetKey(), obj);
		}
	}

	static void LoadXfmrTanks() {
		ResultSet results = DistComponent.RunQuery (DistXfmrTank.szQUERY);
		while (results.hasNext()) {
			DistXfmrTank obj = new DistXfmrTank (results);
			mapTanks.put (obj.GetKey(), obj);
		}
	}

	static void LoadPowerXfmrWindings() {
		ResultSet results = DistComponent.RunQuery (DistPowerXfmrWinding.szQUERY);
		while (results.hasNext()) {
			DistPowerXfmrWinding obj = new DistPowerXfmrWinding (results);
			mapXfmrWindings.put (obj.GetKey(), obj); 
		}
	}

	static void LoadCoordinates() {
		ResultSet results = DistComponent.RunQuery (DistCoordinates.szQUERY);
		while (results.hasNext()) {
			DistCoordinates obj = new DistCoordinates (results);
			mapCoordinates.put (obj.GetKey(), obj);
		}
	}

	public static void PrintOneMap(HashMap<String,? extends DistComponent> map, String label) {
		System.out.println(label);
		SortedSet<String> keys = new TreeSet<String>(map.keySet());
		for (String key : keys) {
			System.out.println (map.get(key).DisplayString());
		}
	}

	public static void PrintAllMaps() {
		// this is the un-sorted method of display
//		for (HashMap.Entry<String,DistBaseVoltage> pair : mapBaseVoltages.entrySet()) {
//			System.out.println (pair.getValue().DisplayString());
//		}

		PrintOneMap (mapBaseVoltages, "** BASE VOLTAGES");
		PrintOneMap (mapCapacitors, "** CAPACITORS");
		PrintOneMap (mapCNCables, "** CN CABLES");
		PrintOneMap (mapCoordinates, "** COMPONENT XY COORDINATES");
		PrintOneMap (mapLinesCodeZ, "** LINES REFERENCING MATRICES");
		PrintOneMap (mapLinesInstanceZ, "** LINES WITH IMPEDANCE ATTRIBUTES");
		PrintOneMap (mapSpacings, "** LINE SPACINGS");
		PrintOneMap (mapLinesSpacingZ, "** LINES REFERENCING SPACINGS");
		PrintOneMap (mapLoads, "** LOADS");
		PrintOneMap (mapWires, "** OVERHEAD WIRES");
		PrintOneMap (mapPhaseMatrices, "** PHASE IMPEDANCE MATRICES");
		PrintOneMap (mapXfmrCores, "** POWER XFMR CORE ADMITTANCES");
		PrintOneMap (mapXfmrMeshes, "** POWER XFMR MESH IMPEDANCES");
		PrintOneMap (mapXfmrWindings, "** POWER XFMR WINDINGS");
		PrintOneMap (mapRegulators, "** REGULATORS");
		PrintOneMap (mapSequenceMatrices, "** SEQUENCE IMPEDANCE MATRICES");
		PrintOneMap (mapSubstations, "** SUBSTATION SOURCES");
		PrintOneMap (mapSwitches, "** LOADBREAK SWITCHES");
		PrintOneMap (mapTSCables, "** TS CABLES");
		PrintOneMap (mapCodeOCTests, "** XFMR CODE OC TESTS");
		PrintOneMap (mapCodeRatings, "** XFMR CODE WINDING RATINGS");
		PrintOneMap (mapCodeSCTests, "** XFMR CODE SC TESTS");
		PrintOneMap (mapTanks, "** XFMR TANKS");
	}

	public static void LoadAllMaps() {
		LoadBaseVoltages();
		LoadCapacitors();
		LoadConcentricNeutralCables();
		LoadCoordinates();
		LoadLinesCodeZ();
		LoadLinesInstanceZ();
		LoadLineSpacings();
		LoadLinesSpacingZ();
		LoadLoads();
		LoadOverheadWires();
		LoadPhaseMatrices();
		LoadPowerXfmrCore();
		LoadPowerXfmrMesh();
		LoadPowerXfmrWindings();
		LoadRegulators();
		LoadSequenceMatrices();
		LoadSubstations();
		LoadSwitches();
		LoadTapeShieldCables();
		LoadXfmrCodeOCTests();
		LoadXfmrCodeRatings();
		LoadXfmrCodeSCTests();
		LoadXfmrTanks();
	}

	public static void WriteJSONSymbolFile (String fXY) throws FileNotFoundException {
		PrintWriter out = new PrintWriter (fXY);
		int count, last;

		out.println("{\"feeder\":[");

		out.println("{\"swing_nodes\":[");
		count = 1;
		last = mapSubstations.size();
		for (HashMap.Entry<String,DistSubstation> pair : mapSubstations.entrySet()) {
			out.print (pair.getValue().GetJSONSymbols(mapCoordinates));
			if (count++ < last) {
				out.println (",");
			} else {
				out.println ("");
			}
		}
		out.println("]},");

		out.println("{\"capacitors\":[");
		count = 1;
		last = mapCapacitors.size();
		for (HashMap.Entry<String,DistCapacitor> pair : mapCapacitors.entrySet()) {
			out.print (pair.getValue().GetJSONSymbols(mapCoordinates));
			if (count++ < last) {
				out.println (",");
			} else {
				out.println ("");
			}
		}
		out.println("]},");

		out.println("{\"overhead_lines\":[");
		count = 1;
		last = mapLinesCodeZ.size() + mapLinesInstanceZ.size() + mapLinesSpacingZ.size();
		for (HashMap.Entry<String,DistLinesCodeZ> pair : mapLinesCodeZ.entrySet()) {
			out.print (pair.getValue().GetJSONSymbols(mapCoordinates));
			if (count++ < last) {
				out.println (",");
			} else {
				out.println ("");
			}
		}
		for (HashMap.Entry<String,DistLinesInstanceZ> pair : mapLinesInstanceZ.entrySet()) {
			out.print (pair.getValue().GetJSONSymbols(mapCoordinates));
			if (count++ < last) {
				out.println (",");
			} else {
				out.println ("");
			}
		}
		for (HashMap.Entry<String,DistLinesSpacingZ> pair : mapLinesSpacingZ.entrySet()) {
			out.print (pair.getValue().GetJSONSymbols(mapCoordinates));
			if (count++ < last) {
				out.println (",");
			} else {
				out.println ("");
			}
		}
		out.println("]},");

		out.println("{\"transformers\":[");
		count = 1;
		last = mapTanks.size();
		for (HashMap.Entry<String,DistXfmrTank> pair : mapTanks.entrySet()) {
			out.print (pair.getValue().GetJSONSymbols(mapCoordinates));
			if (count++ < last) {
				out.println (",");
			} else {
				out.println ("");
			}
		}
		out.println("]},");

		out.println("{\"regulators\":[");
		count = 1;
		last = mapRegulators.size();
		for (HashMap.Entry<String,DistRegulator> pair : mapRegulators.entrySet()) {
			out.print (pair.getValue().GetJSONSymbols(mapCoordinates, mapTanks));
			if (count++ < last) {
				out.println (",");
			} else {
				out.println ("");
			}
		}
		out.println("]}");

		out.println("]}");
		out.close();
	}

	public static void main (String args[]) throws FileNotFoundException {
		String fOut = "", fXY = "";
		double freq = 60.0, load_scale = 1.0;
		boolean bWantSched = false, bWantZIP = false;
		String fSched = "";
		double Zcoeff = 0.0, Icoeff = 0.0, Pcoeff = 0.0;

		if (args.length < 2) {
			System.out.println ("Usage: java CIMImporter [options] output_root");
			System.out.println ("       -l={0..1}          // load scaling factor; defaults to 1");
			System.out.println ("       -f={50|60}         // system frequency; defaults to 60");
			System.out.println ("       -n={schedule_name} // root filename for scheduled ZIP loads (defaults to none)");
			System.out.println ("       -z={0..1}          // constant Z portion (defaults to 0 for CIM-defined LoadResponseCharacteristic)");
			System.out.println ("       -i={0..1}          // constant I portion (defaults to 0 for CIM-defined LoadResponseCharacteristic)");
			System.out.println ("       -p={0..1}          // constant P portion (defaults to 0 for CIM-defined LoadResponseCharacteristic)");
			System.out.println ("Example: java CIMImporter -l=1 -i=1 ieee8500");
			System.out.println ("   assuming Jena and Commons-Math are in Java's classpath, this will produce two output files");
			System.out.println ("   1) ieee8500_base.glm with GridLAB-D components for a constant-current model at peak load");
			System.out.println ("      This file includes an adjustable source voltage, and manual capacitor/tap changer states.");
			System.out.println ("      It should be invoked from a separate GridLAB-D file that sets up the clock, solver, recorders, etc.");
			System.out.println ("      For example, these two GridLAB-D input lines set up 1.05 per-unit source voltage on a 115-kV system:");
			System.out.println ("          #define VSOURCE=69715.065 // 66395.3 * 1.05");
			System.out.println ("          #include \"ieee8500_base.glm\"");
			System.out.println ("      If there were capacitor/tap changer controls in the CIM input file, that data was written to");
			System.out.println ("          ieee8500_base.glm as comments, which can be recovered through manual edits.");
			System.out.println ("   2) ieee8500_symbols.json with component labels and geographic coordinates, used in GridAPPS-D but not GridLAB-D");
			System.out.println ("TODO: implement arguments for SPARQL endpoint and CIM EquipmentContainer selection");
			System.exit (0);
		}

		int i = 0;
		while (i < args.length) {
			if (args[i].charAt(0) == '-') {
				char opt = args[i].charAt(1);
				String optVal = args[i].substring(3);
				if (opt == 'l') {
					load_scale = Double.parseDouble(optVal);
				} else if (opt == 'f') {
					freq = Double.parseDouble(optVal);
				} else if (opt == 'n') {
					fSched = optVal;
					bWantSched = true;
				} else if (opt == 'z') {
					Zcoeff = Double.parseDouble(optVal);
					bWantZIP = true;
				} else if (opt == 'i') {
					Icoeff = Double.parseDouble(optVal);
					bWantZIP = true;
				} else if (opt == 'p') {
					Pcoeff = Double.parseDouble(optVal);
					bWantZIP = true;
				}
			} else {
				fOut = args[i] + "_base.glm";
				fXY = args[i] + "_symbols.json";
			}
			++i;
		}

		LoadAllMaps();

		WriteJSONSymbolFile (fXY);
		PrintWriter out = new PrintWriter (fOut);
		out.close();
//		PrintAllMaps();
	}
}

