/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author 201719050198
 */
public class SymbolTable <T extends STEntry> implements Iterable<T>
{
    SymbolTable<T> parent;
    TreeMap<String, T> symbols;

    SymbolTable()
    {
        symbols = new TreeMap<String, T>();
    }
    
    SymbolTable(SymbolTable<T> p)
    {
        symbols = new TreeMap<String, T>();
        
        parent = p;
    }

    public boolean add(T t)
    {
        if (symbols.containsKey(t.lexeme))
                return false;
        symbols.put(t.lexeme, t);
        return true;
    }

    public boolean remove(String name)
    {
        return symbols.remove(name) != null;
    }

    public void clear()
    {
        symbols.clear();
    }

    public boolean isEmpty()
    {
        return symbols.isEmpty();
    }

    public T get(String name)
    {
        T s;
        SymbolTable<T> table = this;

        do
        {
            s = table.symbols.get(name);
        } while (s == null && (table = table.parent) != null);
        
        return s;
    }

    public Iterator<T> iterator()
    {
        return symbols.values().iterator();
    } 
}
