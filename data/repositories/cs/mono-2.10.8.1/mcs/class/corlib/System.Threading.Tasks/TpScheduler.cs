// TpScheduler.cs
//
// Copyright (c) 2011 Jérémie "Garuma" Laval
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
//
//

#if NET_4_0 || MOBILE
using System;
using System.Collections.Concurrent;

namespace System.Threading.Tasks
{
internal class TpScheduler: TaskScheduler
{
    static readonly WaitCallback callback = TaskExecuterCallback;

    protected internal override void QueueTask (Task task)
    {
        ThreadPool.UnsafeQueueUserWorkItem (callback, task);
    }

    static void TaskExecuterCallback (object obj)
    {
        Task task = (Task)obj;
        task.Execute ();
    }

    protected override System.Collections.Generic.IEnumerable<Task> GetScheduledTasks ()
    {
        throw new System.NotImplementedException();
    }

    protected internal override bool TryDequeue (Task task)
    {
        throw new System.NotImplementedException();
    }

    protected override bool TryExecuteTaskInline (Task task, bool taskWasPreviouslyQueued)
    {
        return TryExecuteTask(task);
    }

    public override int MaximumConcurrencyLevel
    {
        get
        {
            return base.MaximumConcurrencyLevel;
        }
    }
}
}
#endif
