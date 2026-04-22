import {Component, EventEmitter, Output, signal} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TourService} from "../../service/tour";

@Component({
    selector: 'app-upload-import',
    imports: [],
    templateUrl: './upload-import.html',
    styleUrl: './upload-import.scss',
})
export class UploadImport {
    readonly isUploadingFile = signal(false);
    @Output() refreshData = new EventEmitter<void>();

    constructor(private readonly tourService: TourService, private readonly toastr: ToastrService) {
    }

    onJsonFileSelected(event: Event): void {
        event.stopPropagation();

        const input = event.target as HTMLInputElement;
        const file = input.files?.item(0);
        if (!file) {
            return;
        }

        if (file.type && file.type !== "application/json") {
            this.toastr.error("Upload a JSON file!");
            input.value = "";
            return;
        }

        if (this.isUploadingFile()) {
            input.value = "";
            return;
        }

        this.isUploadingFile.set(true);
        const clearUploadState = () => {
            this.isUploadingFile.set(false);
            input.value = "";
        };

        this.tourService.uploadJsonFile(file, () => {
            this.toastr.success("Tours imported!");
            this.refreshData.emit();
            clearUploadState();
        }, clearUploadState);
    }
}
