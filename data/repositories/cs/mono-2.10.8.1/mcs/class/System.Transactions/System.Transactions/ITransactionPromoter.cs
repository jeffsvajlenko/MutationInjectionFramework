//
// ITransactionPromoter.cs
//
// Author:
//	Atsushi Enomoto  <atsushi@ximian.com>
//
// (C)2005 Novell Inc,
//

#if NET_2_0

namespace System.Transactions
{
public interface ITransactionPromoter
{
    byte [] Promote ();
}
}

#endif
