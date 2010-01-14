/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package  org.jasig.portal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Internal adapter for a multithreaded character channel that is also
 * cacheable and implements IMimeResponse (capable of using DonwloadWorker)
 * @author <a href="mailto:nbolton@unicon.net">Nick Bolton</a>
 * @version $Revision$
 * @see MultithreadedCacheableChannelAdapter
 * @deprecated All IChannel implementations should be migrated to portlets
 */
@Deprecated
public class MultithreadedCacheableMimeResponseCharacterChannelAdapter extends MultithreadedCacheableCharacterChannelAdapter
implements IMimeResponse {
    public MultithreadedCacheableMimeResponseCharacterChannelAdapter (IMultithreadedCharacterChannel channel,
    String uid) throws PortalException {
        super(channel, uid);
        if (!(channel instanceof IMultithreadedMimeResponse)) {
            throw  (new PortalException("MultithreadedCacheableMimeResponseChannelAdapter: Cannot adapt "
            + channel.getClass().getName()));
        }
    }
    public String getContentType () {
        return  ((IMultithreadedMimeResponse)channel).getContentType(uid);
    }
    public InputStream getInputStream () throws IOException {
        return  ((IMultithreadedMimeResponse)channel).getInputStream(uid);
    }
    public void downloadData (OutputStream out) throws IOException {
        ((IMultithreadedMimeResponse)channel).downloadData(out, uid);
    }
    public String getName () {
        return  ((IMultithreadedMimeResponse)channel).getName(uid);
    }
    public Map getHeaders () {
        return  ((IMultithreadedMimeResponse)channel).getHeaders(uid);
    }

    public void reportDownloadError(Exception e) {
      ((IMultithreadedMimeResponse)channel).reportDownloadError(e);
    }
}
