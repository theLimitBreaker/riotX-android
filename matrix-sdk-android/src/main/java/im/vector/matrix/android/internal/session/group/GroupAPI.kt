/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.internal.session.group

import im.vector.matrix.android.internal.network.NetworkConstants
import im.vector.matrix.android.internal.session.group.model.GroupRooms
import im.vector.matrix.android.internal.session.group.model.GroupSummaryResponse
import im.vector.matrix.android.internal.session.group.model.GroupUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GroupAPI {

    /**
     * Request a group summary
     *
     * @param groupId the group id
     */
    @GET(NetworkConstants.URI_API_PREFIX_PATH_R0 + "groups/{groupId}/summary")
    fun getSummary(@Path("groupId") groupId: String): Call<GroupSummaryResponse>

    /**
     * Request the rooms list.
     *
     * @param groupId the group id
     */
    @GET(NetworkConstants.URI_API_PREFIX_PATH_R0 + "groups/{groupId}/rooms")
    fun getRooms(@Path("groupId") groupId: String): Call<GroupRooms>


    /**
     * Request the users list.
     *
     * @param groupId the group id
     */
    @GET(NetworkConstants.URI_API_PREFIX_PATH_R0 + "groups/{groupId}/users")
    fun getUsers(@Path("groupId") groupId: String): Call<GroupUsers>


}