package gov.pnnl.goss.cim2glm.components;
//	----------------------------------------------------------
//	Copyright (c) 2017, Battelle Memorial Institute
//	All rights reserved.
//	----------------------------------------------------------

// package gov.pnnl.gridlabd.cim;

import org.apache.jena.query.*;
import java.text.DecimalFormat;

public class DistLoad extends DistComponent {
	public static final String szQUERY = 
	 	"SELECT ?name ?bus ?basev ?p ?q ?conn ?pz ?qz ?pi ?qi ?pp ?qp ?pe ?qe ?id "+
		"(group_concat(distinct ?phs;separator=\"\\n\") as ?phases) "+
		"WHERE {"+
	 	" ?s r:type c:EnergyConsumer."+
	 	" ?s c:IdentifiedObject.name ?name."+
	   " ?s c:ConductingEquipment.BaseVoltage ?bv."+
	   " ?bv c:BaseVoltage.nominalVoltage ?basev."+
	 	" ?s c:EnergyConsumer.pfixed ?p."+
	 	" ?s c:EnergyConsumer.qfixed ?q."+
	 	" ?s c:EnergyConsumer.phaseConnection ?connraw."+
	 	" 			bind(strafter(str(?connraw),\"PhaseShuntConnectionKind.\") as ?conn)"+
	 	" ?s c:EnergyConsumer.LoadResponse ?lr."+
	 	" ?lr c:LoadResponseCharacteristic.pConstantImpedance ?pz."+
	 	" ?lr c:LoadResponseCharacteristic.qConstantImpedance ?qz."+
	 	" ?lr c:LoadResponseCharacteristic.pConstantCurrent ?pi."+
	 	" ?lr c:LoadResponseCharacteristic.qConstantCurrent ?qi."+
	 	" ?lr c:LoadResponseCharacteristic.pConstantPower ?pp."+
	 	" ?lr c:LoadResponseCharacteristic.qConstantPower ?qp."+
	 	" ?lr c:LoadResponseCharacteristic.pVoltageExponent ?pe."+
	 	" ?lr c:LoadResponseCharacteristic.qVoltageExponent ?qe."+
	 	" OPTIONAL {?ecp c:EnergyConsumerPhase.EnergyConsumer ?s."+
	 	" ?ecp c:EnergyConsumerPhase.phase ?phsraw."+
	 	" 			bind(strafter(str(?phsraw),\"SinglePhaseKind.\") as ?phs) }"+
	 	" bind(strafter(str(?s),\"#_\") as ?id)."+
	 	" ?t c:Terminal.ConductingEquipment ?s."+
	 	" ?t c:Terminal.ConnectivityNode ?cn."+
	 	" ?cn c:IdentifiedObject.name ?bus"+
	 	"} "+
		"GROUP BY ?name ?bus ?basev ?p ?q ?conn ?pz ?qz ?pi ?qi ?pp ?qp ?pe ?qe ?id "+
		"ORDER BY ?name";

	public String id;
	public String name;
	public String bus;
	public String phases;
	public String conn;
	public double basev;
	public double p;
	public double q;
	public double pz;
	public double qz;
	public double pi;
	public double qi;
	public double pp;
	public double qp;
	public double pe;
	public double qe;

	private int dss_load_model;
	private boolean bDelta;

	public DistLoad (ResultSet results) {
		if (results.hasNext()) {
			QuerySolution soln = results.next();
			name = SafeName (soln.get("?name").toString());
			id = soln.get("?id").toString();
			bus = SafeName (soln.get("?bus").toString());
			basev = Double.parseDouble (soln.get("?basev").toString());
			phases = OptionalString (soln, "?phases", "ABC");
			phases = phases.replace ('\n', ':');
			conn = soln.get("?conn").toString();
			p = 0.001 * Double.parseDouble (soln.get("?p").toString());
			q = 0.001 * Double.parseDouble (soln.get("?q").toString());
			pz = Double.parseDouble (soln.get("?pz").toString());
			qz = Double.parseDouble (soln.get("?qz").toString());
			pi = Double.parseDouble (soln.get("?pi").toString());
			qi = Double.parseDouble (soln.get("?qi").toString());
			pp = Double.parseDouble (soln.get("?pp").toString());
			qp = Double.parseDouble (soln.get("?qp").toString());
			pe = Double.parseDouble (soln.get("?pe").toString());
			qe = Double.parseDouble (soln.get("?qe").toString());
		}		
		dss_load_model = 8;
		System.out.println (DisplayString());
	}

	public String DisplayString() {
		DecimalFormat df = new DecimalFormat("#0.0000");
		StringBuilder buf = new StringBuilder ("");
		buf.append (name + " @ " + bus + " basev=" + df.format (basev) + " phases=" + phases + " conn=" + conn);
		buf.append (" kw=" + df.format(p) + " kvar=" + df.format(q));
		buf.append (" Real ZIP=" + df.format(pz) + ":" + df.format(pi) + ":" + df.format(pp));
		buf.append (" Reactive ZIP=" + df.format(qz) + ":" + df.format(qi) + ":" + df.format(qp));
		buf.append (" Exponents=" + df.format(pe) + ":" + df.format(qe));
		return buf.toString();
	}

	private void SetDSSLoadModel() {
		if (pe == 1 && qe == 2) {
			dss_load_model = 4;
			return;
		}
		double sum = pz + pi + pp;
		pz = pz / sum;
		pi = pi / sum;
		pp = pp / sum;
		sum = qz + qi + qp;
		qz = qz / sum;
		qi = qi / sum;
		qp = qp / sum;
		if (pz >= 0.999999 && qz >= 0.999999) {
			dss_load_model = 2;
		}	else if (pi >= 0.999999 && qi >= 0.999999) {
			dss_load_model = 5;
		} else if (pp >= 0.999999 && qp >= 0.999999) {
			dss_load_model = 1;
		} else {
			dss_load_model = 8;
		}
		if (conn.equals("D")) {
			bDelta = true;
		} else {
			bDelta = false;
		}
	}

	private String GetZIPV() {
		DecimalFormat df = new DecimalFormat("#0.0000");
		return "[" + df.format(pz) + "," + df.format(pi) + "," + df.format(pp) + "," + df.format(qz)
		 + "," + df.format(qi) + "," + df.format(pp) + ",0.8]";
	}

	public String GetDSS() {
		StringBuilder buf = new StringBuilder ("new Load." + name);
		DecimalFormat df = new DecimalFormat("#0.000");

		SetDSSLoadModel();
		int nphases = DSSPhaseCount(phases, bDelta);
		double kv = 0.001 * basev;
		if (nphases < 2 && !bDelta) { // 2-phase wye load should be line-line for secondary?
			kv /= Math.sqrt(3.0);
		}

		buf.append (" phases=" + Integer.toString(nphases) + " bus1=" + DSSShuntPhases (bus, phases, bDelta) + 
								" conn=" + DSSConn(bDelta) + " kw=" + df.format(p) + " kvar=" + df.format(q) +
								" numcust=1 kv=" + df.format(kv) + " model=" + Integer.toString(dss_load_model));
		if (dss_load_model == 8) {
			buf.append (" zipv=" + GetZIPV());
		}
		buf.append("\n");

		return buf.toString();
	}

	public String GetKey() {
		return name;
	}
}

