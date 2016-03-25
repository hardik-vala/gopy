package golite.symbol;


/**
 * Untyped alias symbol type, where the type is not set (Used as a placeholder in the first pass of
 * the program for symbol table construction).
 */
class UnTypedAliasSymbolType extends SymbolType {

	/** Alias. */
	private String alias;

	/**
	 * Constructor.
	 *
	 * @param alias - Alias
	 */
	public UnTypedAliasSymbolType(String alias) {
		this.alias = alias;
	}

	/**
	 * Getter.
	 */
	public String getAlias() {
		return this.alias;
	}

	@Override
	public String toString() {
		return this.alias;
	}

}
