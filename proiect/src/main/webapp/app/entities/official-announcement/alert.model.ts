import dayjs from 'dayjs/esm';

export interface IAlert {
  id?: number;
  title?: string | null;
  content?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export class Alert implements IAlert {
  constructor(public id?: number, public title?: string | null, public content?: string | null, public createdDate?: dayjs.Dayjs | null) {}
}

export function getAnnouncementIdentifier(announcement: IAlert): number | undefined {
  return announcement.id;
}
