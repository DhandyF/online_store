import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { IWeight } from 'app/shared/model/weight.model';
import { WeightService } from './weight.service';

@Component({
    selector: 'jhi-weight-update',
    templateUrl: './weight-update.component.html'
})
export class WeightUpdateComponent implements OnInit {
    weight: IWeight;
    isSaving: boolean;
    dateDp: any;

    constructor(protected weightService: WeightService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ weight }) => {
            this.weight = weight;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.weight.id !== undefined) {
            this.subscribeToSaveResponse(this.weightService.update(this.weight));
        } else {
            this.subscribeToSaveResponse(this.weightService.create(this.weight));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeight>>) {
        result.subscribe((res: HttpResponse<IWeight>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
