/* This file was generated by SableCC (http://www.sablecc.org/). */

package golite.symbol;

import java.util.*;
import golite.node.*;
import golite.analysis.*;
import golite.util.*;
import golite.exception.*;

public class SymbolTableBuilder extends DepthFirstAdapter
{
    private SymbolTable symbolTable;
    private HashMap<Node, PTypeExpr> typeTable;
    private LineAndPosTracker lineAndPos;

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public HashMap<Node, PTypeExpr> getTypeTable()
    {
        return typeTable;
    }

    @Override
    public void inStart(Start node)
    {
        symbolTable = new SymbolTable();
        lineAndPos = new LineAndPosTracker();
        typeTable = new HashMap<Node, PTypeExpr>();
        symbolTable.enterScope();
        //Initialize boolean types
        symbolTable.addSymbol("true", new ABoolTypeExpr(new TBool()));
        symbolTable.addSymbol("false", new ABoolTypeExpr(new TBool()));
    }

    @Override
    public void outStart(Start node)
    {
        //symbolTable.exitScope();
    }

    @Override
    public void caseAFuncTopDec(AFuncTopDec node)
    {
        symbolTable.addSymbol(node.getId().getText(), node);
    }

    @Override
    public void inASpecTypeSpec(ASpecTypeSpec node)
    {
        if (node.getOptId() instanceof AIdOptId)
        {
            String name = ((AIdOptId) node.getOptId()).getId().getText();
            symbolTable.addSymbol(name, node.getTypeExpr());
            putTypeExpr(name, node.getTypeExpr());
        }
    }

    private void putStructSub(String name, AStructSubStructSub node) {
        for (POptId n: node.getOptId())
        {
            if (n instanceof AIdOptId)
            {
                TId id = ((AIdOptId) n).getId();
                {
                    String newName = name + "." + id.getText();
                    symbolTable.addSymbol(newName, node);
                    typeTable.put(node, getType(node));
                    putTypeExpr(newName, node.getTypeExpr());
                }
            }
        }
    }

    private void putTypeExpr(String name, PTypeExpr node)
    {
        //TODO: Handle other types of type expressions
        if (isStructType(node))
        {
            for (PStructSub a: ((AStructTypeExpr) node).getStructSub())
            {
                putStructSub(name, (AStructSubStructSub) a);
            }
        }
    }

    @Override
    public void outASpecVarSpec(ASpecVarSpec node)
    {
        {
            List<TId> ids = getIds(node);
            List<PExpr> exprs = node.getExpr();
            for (TId e: ids)
            {
                symbolTable.addSymbol(e.getText(), node);
            }
            if (node.getTypeExpr() != null)
            {
                for (PExpr e: exprs)
                {
                    if (typeTable.get(e).getClass() != node.getTypeExpr().getClass())
                    {
                        callTypeCheckException(e, "Expression type does not match declared variable type");
                    }

                }
                for (TId e: ids)
                {
                        typeTable.put(e, node.getTypeExpr());
                }
            }
            else
            {
                for (int i = 0; i < ids.size(); i++)
                {
                    typeTable.put(ids.get(i), typeTable.get(exprs.get(i)));
                }
            }
        }
    }

    @Override //Modified
    public void caseASpecVarSpec(ASpecVarSpec node)
    {
        {
            List<TId> ids = getIds(node);
            for(TId e : ids)
            {
                symbolTable.addSymbol(e.getText(), node);
            }
        }
    }

    private ArrayList<TId> getIds(Node node)
    {
        if (node instanceof ASpecVarSpec)
        {
            ArrayList<POptId> optIds = new ArrayList<POptId>(((ASpecVarSpec) node).getOptId());
            ArrayList<TId> ids = new ArrayList<TId>();
            for (POptId o: optIds)
            {
                if (o instanceof AIdOptId)
                {
                    ids.add(((AIdOptId) o).getId());
                }
            }
            return ids;
        }
        else if (node instanceof ASpecTypeSpec)
        {
            POptId optId = ((ASpecTypeSpec) node).getOptId();
            ArrayList<TId> ids = new ArrayList<TId>();
            if (optId instanceof AIdOptId)
            {
                ids.add(((AIdOptId) optId).getId());
            }
            return ids;
        }
        else if (node instanceof AShortAssignStmt)
        {
            ArrayList<POptId> optIds = new ArrayList<POptId>(((AShortAssignStmt) node).getOptId());
            ArrayList<TId> ids = new ArrayList<TId>();
            for (POptId o: optIds)
            {
                if (o instanceof AIdOptId)
                {
                    ids.add(((AIdOptId) o).getId());
                }
            }
            return ids;
        }
        else if (node instanceof AStructSubStructSub)
        {
            ArrayList<POptId> optIds = new ArrayList<POptId>(((AStructSubStructSub) node).getOptId());
            ArrayList<TId> ids = new ArrayList<TId>();
            for (POptId o: optIds)
            {
                if (o instanceof AIdOptId)
                {
                    ids.add(((AIdOptId) o).getId());
                }
            }
            return ids;
        }
        return new ArrayList<TId>();
    }

    @Override //Modified
    public void caseASpecTypeSpec(ASpecTypeSpec node)
    {
        symbolTable.addSymbol(getIds(node).get(0).getText(), node);
    }

    /* Helper methods */
    private void callTypeCheckException(Node node, String s) {
        String message = "";
        if (node != null) {
            node.apply(lineAndPos);
            message += "[" + lineAndPos.getLine(node) + "," + lineAndPos.getPos(node) + "] ";
        }
        message += s;
        TypeCheckException e = new TypeCheckException(message);
        e.printStackTrace();
        System.exit(1);
    }

    private boolean isBoolType(PTypeExpr node) {
        return node instanceof ABoolTypeExpr;
    }

    private boolean isIntType(PTypeExpr node) {
        return (node instanceof AIntTypeExpr) || (node instanceof ARuneTypeExpr);
    }

    private boolean isFloatType(PTypeExpr node) {
        return node instanceof AFloatTypeExpr;
    }

    private boolean isNumericType(PTypeExpr node) {
        return isIntType(node) || isFloatType(node);
    }

    private boolean isStringType(PTypeExpr node) {
        return node instanceof AStringTypeExpr;
    }

    private boolean isOrderedType(PTypeExpr node) {
        return isNumericType(node) || isStringType(node);
    }

    public boolean isComparableType(PTypeExpr node) {
        return isOrderedType(node) || isBoolType(node);
    }

    private boolean isStructType(PTypeExpr node) {
        return node instanceof AStructTypeExpr;
    }

    private boolean isBaseType(PTypeExpr node) {
        return isBoolType(node) || isNumericType(node) || isStringType(node);
    }

    private boolean isSameType(PTypeExpr node1, PTypeExpr node2) {
        return ((node1 instanceof ABoolTypeExpr) && (node2 instanceof ABoolTypeExpr))
                || ((node1 instanceof AIntTypeExpr) && (node2 instanceof AIntTypeExpr))
                || ((node1 instanceof AFloatTypeExpr) && (node2 instanceof AFloatTypeExpr))
                || ((node1 instanceof ARuneTypeExpr) && (node2 instanceof ARuneTypeExpr))
                || ((node1 instanceof AStringTypeExpr) && (node2 instanceof AStringTypeExpr));
    }

    /* More helper methods */
    private PTypeExpr getType(Node node)
    {
        //Takes in a node and returns the type node from the AST/typeTable
        if (node instanceof AVariableExpr)
        {
            return getType(((AVariableExpr) node).getId());
        }
        else if (node instanceof TId)
        {

            String id = ((TId) node).getText();
            Node declaration = symbolTable.getSymbol(id, node);
            System.out.println(declaration);
            System.out.println(declaration.getClass());
            if (declaration instanceof ASpecVarSpec)
            {
                ASpecVarSpec dec = (ASpecVarSpec) declaration;
                int idx = dec.getOptId().indexOf(id);
                PTypeExpr typeExpr = getType(declaration);
                if (typeExpr != null)
                {
                    return typeExpr;
                }
                else
                {
                    return typeTable.get(dec.getExpr().get(idx));
                }
            }
            else if (declaration instanceof ASpecTypeSpec)
            {
                ASpecTypeSpec dec = (ASpecTypeSpec) declaration;
                System.out.println(dec);
                return dec.getTypeExpr();
            }
            else if (declaration instanceof AStructTypeExpr)
            {
                return (AStructTypeExpr) declaration;
            }
            else if (declaration instanceof AShortAssignStmt)
            {
                AShortAssignStmt dec = (AShortAssignStmt) declaration;
                ArrayList<TId> ids = getIds(dec);
                for (TId i: ids)
                {
                    if (i.getText().equals(id))
                    {
                        int idx = ids.indexOf(i);
                        return typeTable.get(dec.getExpr().get(idx));
                    }
                }
            }
            else if (declaration instanceof AArgArgGroup)
            {
                return typeTable.get(declaration);
            }
        }
        else if (node instanceof AIntLitExpr
                || node instanceof AFloatLitExpr
                || node instanceof ARuneLitExpr
                || node instanceof AOctLitExpr
                || node instanceof AHexLitExpr
                || node instanceof AInterpretedStringLitExpr
                || node instanceof ARawStringLitExpr)
        {
            return typeTable.get(node);
        }
        else if (node instanceof AShortAssignStmt)
        {
            return null;
        }
        else if (node instanceof ASpecVarSpec)
        {
            PTypeExpr type = ((ASpecVarSpec) node).getTypeExpr();
            if (type != null)
            {
                return type;
            }
            return null;
        }
        else if (node instanceof AStructTypeExpr)
        {
            return (AStructTypeExpr) node;
        }
        else if (node instanceof AArgArgGroup)
        {
            return ((AArgArgGroup) node).getTypeExpr();
        }
        else if (node instanceof AFieldExpr)
        {
            return typeTable.get(node);
        }
        else if (node instanceof ATypeCastExpr)
        {
            return ((ATypeCastExpr) node).getTypeExpr();
        }
        else if (node instanceof AArrayElemExpr)
        {
            return typeTable.get(node);
        }
        else if (node instanceof AExprStmt)
        {
            return getType(((AExprStmt) node).getExpr());
        }
        else if (node instanceof AFuncCallExpr)
        {
            String id = ((AFuncCallExpr) node).getId().getText();
            Node decl = symbolTable.getSymbol(id, node);
            if (decl instanceof AFuncTopDec)
            {
                return ((AFuncTopDec) decl).getTypeExpr();
            }
        }
        else
        {   try{
                return typeTable.get(node);
            }
            catch (Exception e)
            {
                System.out.println(node);
                callTypeCheckException(node, "Did not find type for " + node.getClass());
            }
        }
        return null;
    }
}
