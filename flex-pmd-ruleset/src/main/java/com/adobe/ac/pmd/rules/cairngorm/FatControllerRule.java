/**
 *    Copyright (c) 2009, Adobe Systems, Incorporated
 *    All rights reserved.
 *
 *    Redistribution  and  use  in  source  and  binary  forms, with or without
 *    modification,  are  permitted  provided  that  the  following  conditions
 *    are met:
 *
 *      * Redistributions  of  source  code  must  retain  the  above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions  in  binary  form  must reproduce the above copyright
 *        notice,  this  list  of  conditions  and  the following disclaimer in
 *        the    documentation   and/or   other  materials  provided  with  the
 *        distribution.
 *      * Neither the name of the Adobe Systems, Incorporated. nor the names of
 *        its  contributors  may be used to endorse or promote products derived
 *        from this software without specific prior written permission.
 *
 *    THIS  SOFTWARE  IS  PROVIDED  BY THE  COPYRIGHT  HOLDERS AND CONTRIBUTORS
 *    "AS IS"  AND  ANY  EXPRESS  OR  IMPLIED  WARRANTIES,  INCLUDING,  BUT NOT
 *    LIMITED  TO,  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *    PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 *    OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,  INCIDENTAL,  SPECIAL,
 *    EXEMPLARY,  OR  CONSEQUENTIAL  DAMAGES  (INCLUDING,  BUT  NOT  LIMITED TO,
 *    PROCUREMENT  OF  SUBSTITUTE   GOODS  OR   SERVICES;  LOSS  OF  USE,  DATA,
 *    OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *    LIABILITY,  WHETHER  IN  CONTRACT,  STRICT  LIABILITY, OR TORT (INCLUDING
 *    NEGLIGENCE  OR  OTHERWISE)  ARISING  IN  ANY  WAY  OUT OF THE USE OF THIS
 *    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.adobe.ac.pmd.rules.cairngorm;

import java.util.List;

import com.adobe.ac.pmd.nodes.IClass;
import com.adobe.ac.pmd.nodes.IPackage;
import com.adobe.ac.pmd.parser.IParserNode;
import com.adobe.ac.pmd.rules.core.AbstractAstFlexRule;
import com.adobe.ac.pmd.rules.core.ViolationPriority;

/**
 * @author xagnetti
 */
public class FatControllerRule extends AbstractAstFlexRule // NO_UCD
{
   /*
    * (non-Javadoc)
    * @see
    * com.adobe.ac.pmd.rules.core.AbstractAstFlexRule#isConcernedByTheCurrentFile
    * ()
    */
   @Override
   public final boolean isConcernedByTheCurrentFile()
   {
      return getCurrentFile().getClassName().endsWith( "Controller.as" );
   }

   /*
    * (non-Javadoc)
    * @see
    * com.adobe.ac.pmd.rules.core.AbstractAstFlexRule#findViolations(com.adobe
    * .ac.pmd.nodes.IPackage)
    */
   @Override
   protected final void findViolations( final IPackage packageNode )
   {
      final IClass classNode = packageNode.getClassNode();

      if ( classNode != null )
      {
         final int commandsCount = computeCommandsCountInImport( packageNode.getImports() );
         final int methodsCount = classNode.getFunctions().size();

         if ( methodsCount > 0
               && commandsCount
                     / methodsCount > 5 )
         {
            addViolation( classNode );
         }
      }
   }

   /*
    * (non-Javadoc)
    * @see com.adobe.ac.pmd.rules.core.AbstractFlexRule#getDefaultPriority()
    */
   @Override
   protected final ViolationPriority getDefaultPriority()
   {
      return ViolationPriority.NORMAL;
   }

   private int computeCommandsCountInImport( final List< IParserNode > imports )
   {
      int commandImport = 0;

      if ( imports != null )
      {
         for ( final IParserNode importNode : imports )
         {
            if ( importNode.getStringValue().endsWith( "Command" ) )
            {
               commandImport++;
            }
         }
      }
      return commandImport;
   }
}