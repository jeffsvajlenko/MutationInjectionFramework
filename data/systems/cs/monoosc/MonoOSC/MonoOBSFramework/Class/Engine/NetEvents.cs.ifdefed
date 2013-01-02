//
// NetEvents.cs
//
// Author:
//       Surfzoid <surfzoid@gmail.com>
//
// Copyright (c) 2009 Surfzoid
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

using System;
using System.Collections.Generic;
using System.Text;

namespace MonoOBSFramework.Engine
{
/// <summary>
/// Define a class to hold Net event info
/// </summary>
public class NetEventArgs : EventArgs
{
    /// <summary>
    /// This will event network Url activity
    /// </summary>
    /// <param name="s"></param>
    public NetEventArgs(string s)
    {
        message = s;
    }
    private string message;
    /// <summary>
    ///
    /// </summary>
    public string Message
    {
        get
        {
            return message;
        }
        set
        {
            message = value;
        }
    }
}

/// <summary>
/// Class that publishes an event
/// </summary>
public class NetEvents
{

    /// <summary>
    /// Declare the event using EventHandler &lt;T&gt;
    /// </summary>
    public event EventHandler<NetEventArgs> RaiseNetEvent;
    /// <summary>
    ///
    /// </summary>
    /// <param name="SendMess"></param>
    public void DoSomething(string SendMess)
    {
        try
        {
            if (!string.IsNullOrEmpty(SendMess))
            {
                byte[] DTToWrite = Encoding.Default.GetBytes(String.Format("{0} : " + SendMess + Environment.NewLine, DateTime.Now.ToString()));
                VarGlobal.LogFsStream.Write(DTToWrite, 0, DTToWrite.Length);
                VarGlobal.LogFsStream.Flush();
                if (VarGlobal._ShowLogWindow == true) SetText(String.Format("{0} : " + SendMess + Environment.NewLine, DateTime.Now.ToString()));
            }
        }
        catch (Exception ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message);
        }
        // Write some code that does something useful here
        // then raise the event. You can also raise an event
        // before you execute a block of code.
        OnRaiseNetEvent(new NetEventArgs(SendMess));

    }

    delegate void SetTextCallback(string Txt);
    private void SetText(string Txt)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (VarGlobal.LogFrm.InvokeRequired)
        {
            SetTextCallback d = new SetTextCallback(SetText);
            VarGlobal.LogFrm.Invoke(d, Txt);
        }
        else
        {
            try
            {
                if (VarGlobal._ShowLogWindow == true)
                    VarGlobal.LogFrm._AddTxt = Txt;
            }
            catch (Exception ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            }
        }
    }

    /// <summary>
    /// Wrap event invocations inside a protected virtual method
    /// to allow derived classes to override the event invocation behavior
    /// </summary>
    /// <param name="e"></param>
    protected virtual void OnRaiseNetEvent(NetEventArgs e)
    {
        // Make a temporary copy of the event to avoid possibility of
        // a race condition if the last subscriber unsubscribes
        // immediately after the null check and before the event is raised.
        EventHandler<NetEventArgs> handler = RaiseNetEvent;

        // Event will be null if there are no subscribers
        if (handler != null)
        {
            // Format the string to send inside the NetEventArgs parameter
            e.Message += String.Format(" at {0}", DateTime.Now.ToString());

            // Use the () operator to raise the event.
            handler(this, e);
        }
    }
}

//Class that subscribes to an event
class Subscriber
{
    private string id;
    /// <summary>
    ///
    /// </summary>
    /// <param name="ID"></param>
    /// <param name="pub"></param>
    public Subscriber(string ID, NetEvents pub)
    {
        id = ID;
        // Subscribe to the event using C# 2.0 syntax
        pub.RaiseNetEvent += HandleNetEvent;
    }

    // Define what actions to take when the event is raised.
    void HandleNetEvent(object sender, NetEventArgs e)
    {
        if(!VarGlobal.LessVerbose)Console.WriteLine(id + " received this message: {0}", e.Message);
    }
}

/*class Program
{
    static void Main(string[] args)
    {
        Publisher pub = new Publisher();
        Subscriber sub1 = new Subscriber("sub1", pub);
        Subscriber sub2 = new Subscriber("sub2", pub);

        // Call the method that raises the event.
        pub.DoSomething();

        // Keep the console window open
        if(!VarGlobal.LessVerbose)Console.WriteLine("Press Enter to close this window.");
        Console.ReadLine();

    }
}*/

}
