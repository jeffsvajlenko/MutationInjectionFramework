/*
 * 
 * Copyright (c) 2007-2012,
 *   Lingxiao Jiang         <lxjiang@ucdavis.edu>
 *   Ghassan Misherghi      <ghassanm@ucdavis.edu>
 *   Zhendong Su            <su@ucdavis.edu>
 *   Stephane Glondu        <steph@glondu.net>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of California nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
#include <ptree.h>
#include <map>
#include <string>
#include <token-tree-map.h>
#include <clone-context-php.h>
#include <algorithm>
#include <cctype>

using namespace std;

map<string,int> name2id;
map<int,string> id2name;
#ifdef JAVA
string identifierTypeName = "ID_TK";
#else
#ifdef PHP
string identifierTypeName = "T_VARIABLE";
#else
string identifierTypeName = "IDENTIFIER";
#endif
#endif

static const char *cxtNodes[] = {
#ifdef JAVA 
#include "../ptgen/java/jcontextualNodes.h"
#else
#ifdef PHP
#include "../ptgen/php5/phpcontextualNodes.h"
#else
#include "../ptgen/gcc/ccontextualNodes.h"
#endif
#endif
};

/* For debugging use only */
ParseTree* global_tree_for_debugging;

static bool cloneClusterFiltered(TokenTreeMap & cls) {
  /* Some managieral problems caused a lot of confusion about which
     code is for which filters...and the several versions of the
     filters (especially for filter 4) caused the most trouble, the
     data (# bugs detected) in the paper may be a little off (not
     essential though. Fixed). 

     The code for filters should be better organized to avoid the
     above confusions. TODO: use command-line options instead of
     re-compiling the code for enabling different filters. However,
     since the current code for filtering is scattered among serveral source
     files, it'd better to re-architect the filtering code before
     implementing the command line options.
  */

  if ( cls.rank>=4 /* <=> cls.buggy_score[1]>0 */ && cls.buggy_score[1]>2 ) /* Filter ALL */
    cls.rank -= 4;
  if ( cls.rank==0 // || cls.buggy_score[0]==0
       // individual filters should or should not preserve cases where other things are buggy?
       || ( /* cls.rank<4 && */ cls.buggy_score[0]==-8 ) /* Filter 1: none vs. none. 1 */
       || ( /* cls.rank<4 && */ (cls.buggy_score[0]==-4 || cls.buggy_score[0]==-5) ) /* Filter 2: loop/switch vs. none; deep "if" vs. none. 7.2 */
       || ( /* cls.rank<4 && */ (cls.buggy_score[0]==0 || cls.buggy_score[0]==1) && cls.buggy_score[3]<1 ) /* Filter 3: loop vs. cond+none. 7.1 */
       // filter 4 is in token-tree-map.C. 
       || ( /* cls.rank==4 && */ cls.buggy_score[1]>2 ) /* Filter 5: nVARs differ too much. 4.1 */
       || ( cls.buggy_score[2]<10 ) /* Filter 6: clones are too spatially close. 4.2 */
       )
    return true;
  else
    return false;
}

/* Decide whether to filter a clone cluster: for ONE cluster only;
   use a shell script to pass every clone cluster to this class. */
int main( int argc, char **argv )
{
  if ( argc>2 ) {
    cerr << "Usage: " << argv[0] << " [filter ID=0] [a clone cluster from stdin]" << endl
	 << "\t stdin is the input channel; stdout is the output channel." << endl;
    return 1;
  }
  cerr << "NOTE: Command line options for filter IDs not implemented" << endl;
/*
  int fid = 0;
  if ( argc==2 )
    fid = atoi(argv[1]);
  if ( fid<0 || fid>2 )
    fid = 0;
*/
  id_init();
  TokenTreeMap::init_shared_data();
#ifdef JAVA
  TokenTreeMap_Java tt;
#else
#ifdef PHP
  ContextInconsistency_PHP tt;
#else
  TokenTreeMap tt;
#endif
#endif
  tt.initNodes(cxtNodes);
  tt.createFN2Tree();

/*
  bool filtered = false;
  switch (fid) {
  case 0:			// iff all filters agree:
    filtered = tt.isAllFiltered();
    break;
  case 1:
    isFilteredI(tt, 1, filtered);
    break;
  case 2:
    isFilteredI(tt, 2, filtered);
    break;
  case 3:
    isFilteredI(tt, 3, filtered);
    break;
  default:
    filtered = tt.isAnyFiltered();
    break;
  }
*/

  if ( !cloneClusterFiltered(tt) ) {
#ifdef HTML
    cout << "Rank score: " << tt.rank << " * " << tt.clusterbuffer.size() << " =" << tt.rank*tt.clusterbuffer.size()
       << "  buggy score: ";
    for (int i=0; i < NUM_BUGGY_SCORES; i++)
      cout << tt.buggy_score[i] << " ";
    cout << endl;
    for (int i=0; i < tt.clusterbuffer.size(); i++) {
      tt.clusterbuffer[i].out2html(cout);
    }
    cout << "<p></p>" << endl;
#else
    tt.outputCluster(cout);
    cout << endl;
#endif
  }

  return 0;
}

