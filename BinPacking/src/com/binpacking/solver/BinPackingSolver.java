package com.binpacking.solver;

import com.binpacking.io.InstanceReader;
import com.binpacking.model.BinPacking;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class BinPackingSolver {
	
	public static GRBEnv env;
    public static GRBModel model;
    public GRBVar[] y;
    public GRBVar[][] x;
    
    
    public void solver(BinPacking binPacking, String logName) throws GRBException {
    	env = new GRBEnv(logName+".log");
        model = new GRBModel(env);
        model.getEnv().set(GRB.DoubleParam.TimeLimit, 10*60);
        buildModel(binPacking);
        model.write(logName+".lp");
        model.optimize();
        model.dispose();
        env.dispose();
    }
	
	private void buildModel(BinPacking binPacking) throws GRBException {
		y = new GRBVar[binPacking.getNumberOfItems()];
		x = new GRBVar[binPacking.getNumberOfItems()][binPacking.getNumberOfItems()];
		for (int i = 0; i < binPacking.getNumberOfItems(); i++) {
			y[i] = model.addVar(0, 1, 0.0f, GRB.BINARY, "y[" + i + "]");
			for (int j = 0; j < binPacking.getNumberOfItems(); j++) {
				x[i][j] = model.addVar(0, 1, 0.0f, GRB.BINARY, "x[" + i + "]["+j+"]");
			}
		}
		model.update();
		
		GRBLinExpr obj = new GRBLinExpr();
		for (int i = 0; i < binPacking.getNumberOfItems(); i++) {
			obj.addTerm(1.0, y[i]);
		}
		model.setObjective(obj);
		model.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);
		
		for (int i = 0 ; i < binPacking.getNumberOfItems(); i++) {
			GRBLinExpr expr = new GRBLinExpr();
			for (int j = 0; j < binPacking.getNumberOfItems(); j++) {
		    	expr.addTerm(binPacking.getWeightOfItems().get(j), x[i][j]);
			}
			GRBLinExpr exprResult = new GRBLinExpr();
			exprResult.addTerm(binPacking.getBinCapacity(), y[i]);
			model.addConstr(expr, GRB.LESS_EQUAL, exprResult, "");
		}
		
		for (int j = 0; j < binPacking.getNumberOfItems(); j++) {
			GRBLinExpr expr = new GRBLinExpr();
			for (int i = 0 ; i < binPacking.getNumberOfItems(); i++) {
				expr.addTerm(1, x[i][j]);
			}
			model.addConstr(expr, GRB.EQUAL, 1, "");
		}
		
		for (int i = 0 ; i < binPacking.getNumberOfItems()-1; i++) {
			model.addConstr(y[i+1], GRB.LESS_EQUAL, y[i], "");
		}
		
		//model.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);
		model.update();
		
	}

	public static void main(String[] args) {
		BinPacking bin0 = InstanceReader.build("instances/instance0.bpp");
		BinPacking bin1 = InstanceReader.build("instances/instance1.bpp");
		BinPacking bin2 = InstanceReader.build("instances/instance2.bpp");
		BinPacking bin3 = InstanceReader.build("instances/instance3.bpp");
		BinPacking bin4 = InstanceReader.build("instances/instance4.bpp");
		BinPacking bin5 = InstanceReader.build("instances/instance5.bpp");
		BinPacking bin6 = InstanceReader.build("instances/instance6.bpp");
		BinPacking bin7 = InstanceReader.build("instances/instance7.bpp");
		BinPacking bin8 = InstanceReader.build("instances/instance8.bpp");
		BinPacking bin9 = InstanceReader.build("instances/instance9.bpp");
		try {
			new BinPackingSolver().solver(bin0, "bin-solver0");
			new BinPackingSolver().solver(bin1, "bin-solver1");
			new BinPackingSolver().solver(bin2, "bin-solver2");
			new BinPackingSolver().solver(bin3, "bin-solver3");
			new BinPackingSolver().solver(bin4, "bin-solver4");
			new BinPackingSolver().solver(bin5, "bin-solver5");
			new BinPackingSolver().solver(bin6, "bin-solver6");
			new BinPackingSolver().solver(bin7, "bin-solver7");
			new BinPackingSolver().solver(bin8, "bin-solver8");
			new BinPackingSolver().solver(bin9, "bin-solver9");
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

}
