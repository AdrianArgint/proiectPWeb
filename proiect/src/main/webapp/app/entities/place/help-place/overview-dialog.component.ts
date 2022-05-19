import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'overview-dialog',
  templateUrl: './overview-dialog.component.html',
})
export class OverviewDialogComponent {
  constructor(public dialogRef: MatDialogRef<OverviewDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
