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

package im.vector.matrix.android.internal.database.query

import im.vector.matrix.android.internal.database.model.ReadReceiptsSummaryEntity
import im.vector.matrix.android.internal.database.model.ReadReceiptsSummaryEntityFields
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.kotlin.where

internal fun ReadReceiptsSummaryEntity.Companion.where(realm: Realm, eventId: String): RealmQuery<ReadReceiptsSummaryEntity> {
    return realm.where<ReadReceiptsSummaryEntity>()
            .equalTo(ReadReceiptsSummaryEntityFields.EVENT_ID, eventId)
}

internal fun ReadReceiptsSummaryEntity.Companion.whereInRoom(realm: Realm, roomId: String?): RealmQuery<ReadReceiptsSummaryEntity> {
    val query = realm.where<ReadReceiptsSummaryEntity>()
    if (roomId != null) {
        query.equalTo(ReadReceiptsSummaryEntityFields.ROOM_ID, roomId)
    }
    return query
}
