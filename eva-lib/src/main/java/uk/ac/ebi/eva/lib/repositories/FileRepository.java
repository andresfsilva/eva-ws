/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.lib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.eva.lib.models.FileFtpReference;
import uk.ac.ebi.eva.lib.entities.File;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    Long countByFileTypeIn(List<String> strings);

    //named query
    FileFtpReference getFileFtpReferenceByFilename(@Param("filename") String filename);

    //named query
    List<FileFtpReference> getFileFtpReferenceByNames(@Param("filenames") List<String> filenames);
}
